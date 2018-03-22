package com.galigaigai.jobbridge.controller;

import com.galigaigai.jobbridge.model.Company;
import com.galigaigai.jobbridge.model.Student;
import com.galigaigai.jobbridge.repository.CompanyRepository;
import com.galigaigai.jobbridge.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    @Autowired
    private CompanyRepository companyRepository;

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
        model.addAttribute("userName",((Student)loginUser).getUserName());

        return "auditCompany";
    }

    /**
     * 管理员处理该公司的审核信息
     *
     * 在前端post的信息中格式为
     * "companyId":companyId,
     * "auditStatus":true/false
     * true表示通过审核
     * false表示拒绝这次审核提交
     */
    @PostMapping(value = "/audit_company")
    public void adminHandleAuditEvent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !("a".equals(((Student)loginUser).getIdentity()))){
            response.sendRedirect("/");
        }

        Object objectCompanyId = request.getParameter("companyId");
        Object objectAuditStatus = request.getParameter("auditStatus");
        if (objectCompanyId == null || objectAuditStatus == null){
            System.out.println("objectCompanyId == null || objectAuditStatus == null");
            response.sendRedirect("/");
        }else{
            Long companyId = Long.parseLong(objectCompanyId.toString());
            Boolean AuditStatus = Boolean.parseBoolean(objectAuditStatus.toString());
            //通过审核，即将公司的auditing字段修改为true
            if (AuditStatus){
                companyService.updateAuditingByCompanyId(companyId);

            }else{
                //打回审核,也就是将该公司的信息在数据库中删除
                companyService.deleteCompanyByCompanyId(companyId);
            }

        }
    }


}
