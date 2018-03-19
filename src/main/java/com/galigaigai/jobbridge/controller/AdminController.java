package com.galigaigai.jobbridge.controller;

import com.galigaigai.jobbridge.model.Company;
import com.galigaigai.jobbridge.model.Student;
import com.galigaigai.jobbridge.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

/**
 * Created by HanrAx on 2018/3/13 0013.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CompanyService companyService;

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

    /**
     * 管理员审核公司页面以及列表数据
     */
    @GetMapping(value = "/audit_company")
    public String adminAuditCompany(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !("a".equals(((Student)loginUser).getIdentity()))){
            response.sendRedirect("/");
        }
        List<Company> companyList = companyService.findCompanyAuditOrNot(false);
        model.addAttribute("companyList",companyList);


        return "auditCompany";
    }

}
