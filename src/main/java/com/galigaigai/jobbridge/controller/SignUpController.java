package com.galigaigai.jobbridge.controller;

import com.galigaigai.jobbridge.model.Company;
import com.galigaigai.jobbridge.model.Student;
import com.galigaigai.jobbridge.util.Crypto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by HanrAx on 2018/3/13 0013.
 */
@Controller
public class SignUpController {

    /**
     * 请求注册页面
     * @param response
     * @return signUp.html
     */
    @GetMapping("/sign_up")
    public String showSignUp(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        return "signUp";
    }

    /**
     * 执行注册操作
     * @param request
     * @param response
     * @throws Exception
     */
    @PostMapping("/sign_up")
    public void doSignUp(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        String result = "";
        String userName = request.getParameter("userName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String identity = request.getParameter("identity");
        Object stu,com;
        int registerStatus = 1;
        System.out.println("password: "+password);
        stu = studentRepository.findByUserName(userName);
        com = enterpriseRepository.findByUserName(userName);
        if (com != null || stu != null){
            result = "{\"ok\":\"false\",\"reason\":\"该用户名已被其他人注册\"}";
            registerStatus = 0;
        }
        if (email != null || !"".equals(email)){
            stu = studentRepository.findByMailbox(email);
            com = enterpriseRepository.findByMailbox(email);
            if (com != null || stu != null){
                result = "{\"ok\":\"false\",\"reason\":\"该邮箱已被其他人注册\"}";
                registerStatus = 0;
            }
        }


        if (registerStatus == 1) {
            String cryptoPassword = Crypto.getEncryptedPwd(password);
            result = "{\"ok\":\"true\"}";
            switch (identity) {
                case "s":
                    Student student = new Student(userName, email, cryptoPassword, identity);
                    studentService.addStudent(student);
                    break;
                case "a":
                    Student manager = new Student(userName, "", cryptoPassword, identity);
                    studentService.addStudent(manager);
                    break;
                case "e":
                    String name = request.getParameter("name");
                    String phoneNum = request.getParameter("phoneNum");
                    String enterpriseIntroduction = request.getParameter("enterpriseIntroduction");
                    Company company = new Company(userName, name, email, phoneNum, cryptoPassword, enterpriseIntroduction, "", "e");
                    enterpriseService.addEnterprise(company);
                    break;
            }
        }

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }
}
