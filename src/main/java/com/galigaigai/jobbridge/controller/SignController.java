package com.galigaigai.jobbridge.controller;

import com.galigaigai.jobbridge.model.Company;
import com.galigaigai.jobbridge.model.Student;
import com.galigaigai.jobbridge.repository.CompanyRepository;
import com.galigaigai.jobbridge.repository.StudentRepository;
import com.galigaigai.jobbridge.service.CompanyService;
import com.galigaigai.jobbridge.service.StudentService;
import com.galigaigai.jobbridge.util.CryptoUtil;
import com.galigaigai.jobbridge.util.FileUploadUtil;
import com.galigaigai.jobbridge.util.SendInfoUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by HanrAx on 2018/3/13 0013.
 * 该Controller实现登录注册登出
 *
 */
@Controller
public class SignController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CompanyService companyService;

    /**
     * 网站首页
     * @param response
     * @return
     */
    @GetMapping(value = "/")
    public String showIndex(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");

        if (loginUser == null) {
            //response.sendRedirect("/");
        }else{
            System.out.println(">>>  loginUser");
            model.addAttribute("loginUser", loginUser);
        }


        return "index";
    }


    /**
     * 请求登录页面
     * @return 返回signIn.html
     */
    @GetMapping("/sign_in")
    public String showSignIn(HttpServletRequest request, HttpServletResponse response, Model model){
        response.setHeader("Access-Control-Allow-Origin", "*");
        String timestamp = Long.toString(System.currentTimeMillis());
        request.getSession().setAttribute("timestamp",timestamp);
        String originPage = request.getParameter("originPage");
        model.addAttribute("originPage",originPage);
        return "signIn";
    }

    /**
     * 执行登陆操作
     * @throws Exception
     */
    @PostMapping("/sign_in")
    public void doSignIn(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String result;
        response.setHeader("Access-Control-Allow-Origin", "*");
        String loginName = request.getParameter("userName");
        String password = request.getParameter("passWord");
        String originPage = request.getParameter("originPage");

        Object stu,com;

        if(loginName.contains("@")){
            stu = studentRepository.findByMailbox(loginName);
            com = companyRepository.findByMailbox(loginName);
        }else{
            stu = studentRepository.findByUserName(loginName);
            com = companyRepository.findByUserName(loginName);
        }
        JSONObject jsonData = new JSONObject();
        if("recruitSea".equals(originPage)){
            jsonData.put("url","/recruit/info");
        }else{
            jsonData.put("url","/");
        }
        if (stu != null){
            Student student = (Student) stu;
            if(!CryptoUtil.validPassword(password, student.getPassword())){
//                result = "{\"ok\":\"false\",\"reason\":\"用户不存在或密码错误\"}";     //密码错误
                jsonData.put("ok","false");
                jsonData.put("reason","用户不存在或密码错误");
            }else{
//                result = "{\"ok\":\"true\",\"identity\":\""+student.getIdentity()+"\"}";
                jsonData.put("ok","true");
                jsonData.put("identity",student.getIdentity());
                request.getSession().setAttribute("loginUser",student);
            }
        }else if (com != null){
            Company company = (Company)com;
            if(!CryptoUtil.validPassword(password, company.getPassword())){
//                result = "{\"ok\":\"false\",\"reason\":\"用户不存在或密码错误\"}";     //密码错误
                jsonData.put("ok","false");
                jsonData.put("reason","用户不存在或密码错误");
            }else{
//                result = "{\"ok\":\"true\",\"identity\":\""+company.getIdentity()+"\"}";
                jsonData.put("ok","true");
                jsonData.put("identity",company.getIdentity());
                request.getSession().setAttribute("loginUser",company);
            }
        }else{
//            result = "{\"ok\":\"false\",\"reason\":\"用户不存在或密码错误\"}";     //用户不存在
            jsonData.put("ok","false");
            jsonData.put("reason","用户不存在或密码错误");
        }

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
//        out.print(result);
        out.print(jsonData.toString());
        out.flush();
        out.close();
    }

    /**
     * 请求注册页面
     * @return signUp.html
     */
    @GetMapping("/sign_up")
    public String showSignUp(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return "signUp";
    }

    /**
     * 执行注册操作
     * @throws Exception
     */
    @PostMapping("/sign_up")
    public void doSignUp(@RequestParam(value = "img_file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        String result = "";
        String userName = request.getParameter("userName");
        String mailbox = request.getParameter("mailbox");
        String password = request.getParameter("password");
        String identity = request.getParameter("identity");
        Object stu,com;
        int registerStatus = 1;
        System.out.println("password: "+password);
        stu = studentRepository.findByUserName(userName);
        com = companyRepository.findByUserName(userName);
        if (com != null || stu != null){
            result = "{\"ok\":\"false\",\"reason\":\"该用户名已被其他人注册\"}";
            registerStatus = 0;
        }
        if (mailbox != null || !"".equals(mailbox)){
            stu = studentRepository.findByMailbox(mailbox);
            com = companyRepository.findByMailbox(mailbox);
            if (com != null || stu != null){
                result = "{\"ok\":\"false\",\"reason\":\"该邮箱已被其他人注册\"}";
                registerStatus = 0;
            }
        }


        if (registerStatus == 1) {
            String cryptoPassword = CryptoUtil.getEncryptedPwd(password);
            result = "{\"ok\":\"true\"}";
            switch (identity) {
                case "s":
                    Student student = new Student(userName, mailbox, cryptoPassword, identity);
                    studentService.addStudent(student);
                    break;
                case "a":
                    Student manager = new Student(userName, "", cryptoPassword, identity);
                    studentService.addStudent(manager);
                    break;
                case "e":
                    //处理公司logo上传
                    if (!file.isEmpty()) {
                        try {
                            //获取上传文件存放的目录,无则创建
                            FileUploadUtil.uploadFile(file);
                            String name = request.getParameter("name");
                            String phoneNum = request.getParameter("phoneNum");
                            String companyIntroduction = request.getParameter("companyIntroduction");
                            Integer industryId = Integer.parseInt(request.getParameter("industryId"));
                            String iconAddress = "/img/comlogo/"+file.getOriginalFilename();
                            Company company = new Company(userName, name, mailbox, phoneNum, cryptoPassword, companyIntroduction, iconAddress,industryId, "e", false);
                            companyService.addCompany(company);
                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
            }
        }

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    /**
     * 执行登出操作
     * @throws Exception
     */
    @GetMapping("/sign_out")
    public void doSignOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null){
            response.sendRedirect("/");
            return;
        }
        request.getSession().removeAttribute("loginUser");
        response.sendRedirect("/");

    }

    /**
     * 在主页中显示用户名
     */
    @GetMapping(value = "/username")
    public void showUserName(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null){
            response.sendRedirect("/");
            return;
        }
        String userName;
        if(loginUser instanceof Student){
            userName = ((Student) loginUser).getUserName();
        }else if(loginUser instanceof Company){
            userName = ((Company)loginUser).getUserName();
        }else{
            userName = "";
        }
        String result = "{\"username\":\"" + userName + "\"}";
        SendInfoUtil.render(result,"text/json",response);
    }
}
