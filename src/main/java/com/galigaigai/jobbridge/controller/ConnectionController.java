package com.galigaigai.jobbridge.controller;

import com.galigaigai.jobbridge.model.Student;
import com.galigaigai.jobbridge.model.StudentDetail;
import com.galigaigai.jobbridge.repository.StudentDetailRepository;
import com.galigaigai.jobbridge.repository.StudentRepository;
import com.galigaigai.jobbridge.service.StudentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HanrAx on 2018/3/22 0022.
 */
@Controller
@RequestMapping("/connection")
public class ConnectionController {
    @Autowired
    public StudentDetailRepository studentDetailRepository;
    @Autowired
    private StudentDetailService studentDetailService;
    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/index")
    public String showConnection(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !("s".equals(((Student)loginUser).getIdentity()))){
            response.sendRedirect("/"); // 修改浏览器的URL
            return "index";
        }
        StudentDetail studentDetail1 = studentDetailRepository.findByStudentId(((Student)loginUser).getStudentId());
        List<StudentDetail> studentDetailList = studentDetailService.findStudentDetailByStudentDetail(studentDetail1);
        List<String> studentNameList = new ArrayList<>();
        for (StudentDetail tempStudentDetail : studentDetailList){
            Student student = studentRepository.findByStudentId(tempStudentDetail.getStudentId());
            studentNameList.add(student.getUserName());
        }
        model.addAttribute("studentNameList",studentNameList);
        model.addAttribute("studentDetailList",studentDetailList);
        model.addAttribute("userName",((Student) loginUser).getUserName());

        return "connection";

    }
}
