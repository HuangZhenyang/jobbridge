package com.galigaigai.jobbridge.controller;

import com.galigaigai.jobbridge.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by HanrAx on 2018/3/13 0013.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    /*
   * 管理员添加公司企业界面
   * */
    @GetMapping(value = "/add_company")
    public String adminAddCompany(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !("a".equals(((Student)loginUser).getIdentity()))){
            response.sendRedirect("/");
        }
        return "addCompany";
    }
    /*
    * 管理员添加新管理员界面
    * */
    @GetMapping(value = "/add_admin")
    public String adminAddAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !("a".equals(((Student)loginUser).getIdentity()))){
            response.sendRedirect("/");
        }
        return "addAdmin";
    }

}
