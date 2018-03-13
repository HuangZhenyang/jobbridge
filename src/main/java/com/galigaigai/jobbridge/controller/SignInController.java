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
public class SignInController {



    /**
     * 请求登录页面
     * @param request
     * @param response
     * @return 返回signIn.html
     */
    @GetMapping("/sign_in")
    public String showSignIn(HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin", "*");
        String timestamp = Long.toString(System.currentTimeMillis());
        request.getSession().setAttribute("timestamp",timestamp);
        return "signIn";
    }

    /**
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @PostMapping("sign_in")
    public void doSignIn(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String result;
        response.setHeader("Access-Control-Allow-Origin", "*");
        String loginName = request.getParameter("userName");
        String password = request.getParameter("passWord");

        Object stu,com;

        if(loginName.contains("@")){
            stu = studentRepository.findByMailbox(loginName);
            com = enterpriseRepository.findByMailbox(loginName);
        }else{
            stu = studentRepository.findByUserName(loginName);
            com = enterpriseRepository.findByUserName(loginName);
        }

        if (stu != null){
            Student student = (Student) stu;
            if(!Crypto.validPassword(password, student.getPassword())){
                result = "{\"ok\":\"false\",\"reason\":\"用户不存在或密码错误\"}";     //密码错误
            }else{
                result = "{\"ok\":\"true\",\"identity\":\""+student.getIdentity()+"\"}";
                request.getSession().setAttribute("loginUser",student);
            }
        }else if (com != null){
            Company company = new Company();
            if(!Crypto.validPassword(password, company.getPassword())){
                result = "{\"ok\":\"false\",\"reason\":\"用户不存在或密码错误\"}";     //密码错误
            }else{
                result = "{\"ok\":\"true\",\"identity\":\""+company.getIdentity()+"\"}";
                request.getSession().setAttribute("loginUser",company);
            }
        }else{
            result = "{\"ok\":\"false\",\"reason\":\"用户不存在或密码错误\"}";     //用户不存在
        }


        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }


}
