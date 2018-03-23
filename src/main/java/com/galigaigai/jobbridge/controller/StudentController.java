package com.galigaigai.jobbridge.controller;

import com.galigaigai.jobbridge.model.*;
import com.galigaigai.jobbridge.model.Tag;
import com.galigaigai.jobbridge.repository.*;
import com.galigaigai.jobbridge.service.*;
import com.galigaigai.jobbridge.util.*;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by SYunk on 2018/3/13.
 * 处理学生的操作类
 */
@Controller
@RequestMapping(value = "/student")
public class StudentController {

    //    数据库操作依赖注入
    @Autowired
    private StudentDetailRepository studentDetailRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ResumeSendRepository resumeSendRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RecruitRepository recruitRepository;

    @Autowired
    private StarCompanyRepository starCompanyRepository;

    @Autowired
    private StarTagRepository starTagRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private StudentDetailService studentDetailService;

    @Autowired
    private StarTagService starTagService;

    @Autowired
    private TagService tagService;

    @Autowired
    private StarCompanyService starCompanyService;

    @Autowired
    private ResumeSendService resumeSendService;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private RecruitTagRepository recruitTagRepository;

    /**
     * 请求学生简历页面
     */
    @GetMapping(value = "/resume")
    public String studentResume(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
            return "index";
        }
        Student student = (Student) loginUser;
        model.addAttribute("student",student);
        return "resume";
    }

    /**
     * 学生请求具体招聘信息页面
     */
    @GetMapping(value = "/recruit")
    public String studentRecruit(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/sign_in?originPage=recruitSea");
            return "signIn";
        }
        Student student = (Student) loginUser;
        model.addAttribute("student",student);
        Long recruitId;
        if (request.getParameter("id") == null) {
            System.out.println("getParameter error");
        } else {
            recruitId = Long.parseLong(request.getParameter("id"));
            System.out.println(recruitId);
            String haveStar = "false";
            // 查找招聘信息
            Recruit recruit = recruitRepository.findByRecruitId(recruitId);
            if(recruit == null){
                return "recruit";
            }
            model.addAttribute("recruit",recruit);
            // 查找公司
            Company company = companyRepository.findByCompanyId(recruit.getCompanyId());
            if(company == null){
                return "recruit";
            }
            model.addAttribute("company",company);
            // 判定是否已经收藏
            List<StarCompany> starCompanyList = starCompanyRepository.findByStudentId(student.getStudentId());
            if (starCompanyList == null || starCompanyList.isEmpty()
                    || (starCompanyList.size() == 1 && starCompanyList.get(0) == null)) {
                System.out.println("starCompanyList is error");
                return "recruit";
            }
            for (StarCompany starCompany : starCompanyList) {
                if (starCompany.getCompanyId() == recruit.getCompanyId()) {
                    haveStar = "true";
                    break;
                }
            }
            model.addAttribute("haveStar",haveStar);
        }
        return "recruit";
    }

    /**
     * 学生请求验证页面
     */
    @GetMapping(value = "/authentication")
    public String studentAuthentication(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
            return "index";
        }
        Student student = (Student) loginUser;
        model.addAttribute("student",student);
        StudentDetail studentDetail = studentDetailRepository.findByStudentId(student.getStudentId());
        if(studentDetail == null){
            model.addAttribute("authentication","false");
            System.out.println("请求验证页面时，学生未填写详细信息");
            return "studentAuthentication";
        }
        Boolean studentAuthenticationState = studentDetail.getAuthentication(); // 当前学生的验证状态
        model.addAttribute("authentication",studentAuthenticationState.toString());
        return "studentAuthentication";
    }

    /**
     * 请求学生信息页面
     */
    @GetMapping(value = "/info")
    public String studentInfo(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
            return "index";
        }
        Student student = (Student) loginUser;

        model.addAttribute("student",student);
        //通过studentId获取student中的email
        String mailbox = student.getMailbox();
        model.addAttribute("mailbox",mailbox);

        //        查找意向字典
        List<City> cityList = cityRepository.findAll();
        List<Industry> industryList = industryRepository.findAll();
        List<Tag> tagList = tagRepository.findAll();
//        转化为string数组
        String[] cityDictionary = new String[cityList.size()];
        String[] industryDictionary = new String[industryList.size()];
        String[] functionDictionary = new String[tagList.size()];

        String[] intentionCityVector = new String[cityList.size()];
        String[] intentionIndustryVector = new String[industryList.size()];
        String[] intentionFunctionVector = new String[tagList.size()];

        for(int i = 0;i < cityList.size();i++){
            intentionCityVector[i] = "0";
            cityDictionary[i] = cityList.get(i).getName();
        }
        for(int i = 0;i < industryList.size();i++){
            intentionIndustryVector[i] = "0";
            industryDictionary[i] = industryList.get(i).getName();
        }
        for(int i = 0;i < tagList.size();i++){
            intentionFunctionVector[i] = "0";
            functionDictionary[i] = tagList.get(i).getName();
        }

        model.addAttribute("cityDictionary",cityDictionary);
        model.addAttribute("industryDictionary",industryDictionary);
        model.addAttribute("functionDictionary",functionDictionary);

        //通过studentID获取studentDetail中的其他信息
        StudentDetail studentDetail = studentDetailRepository.findByStudentId(student.getStudentId());
        if (studentDetail == null) {
            model.addAttribute("intentionCity",intentionCityVector);
            model.addAttribute("intentionIndustry",intentionIndustryVector);
            model.addAttribute("intentionFunction",intentionFunctionVector);
            return "studentInfo";
        }
//        找到该学生的意向
        String[] intentionCity = ParseStringUtil.parseString(studentDetail.getIntentionCity());
        String[] intentionIndustry = ParseStringUtil.parseString(studentDetail.getIntentionIndustry());
        String[] intentionFunction = ParseStringUtil.parseString(studentDetail.getIntentionFunction());

//        调用函数，得到学生意向向量表
        intentionCityVector = StudentInfoUtil.getStudentIntention(cityDictionary,intentionCity);
        intentionIndustryVector = StudentInfoUtil.getStudentIntention(industryDictionary,intentionIndustry);
        intentionFunctionVector = StudentInfoUtil.getStudentIntention(functionDictionary,intentionFunction);

//        返回渲染数据
        model.addAttribute("studentDetail",studentDetail);
        model.addAttribute("intentionCity",intentionCityVector);
        model.addAttribute("intentionIndustry",intentionIndustryVector);
        model.addAttribute("intentionFunction",intentionFunctionVector);
        return "studentInfo";
    }

    /**
     * 请求学生投递信息页面
     */
    @GetMapping(value = "/resume_send")
    public String studentResumeSend(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
            return "index";
        }
        Student student = (Student) loginUser;
        model.addAttribute("student",student);
//        定义数据结构
        List<String> timeList = new ArrayList<>();
        List<String> companyNameList = new ArrayList<>();
        List<Recruit> recruitList = new ArrayList<>();
//        查询学生投递信息:
//        1.先找到学生对应的简历号
        Resume resume = resumeRepository.findByStudentId(student.getStudentId());
//        如果学生没写简历，则返回空数据
        if (resume == null) {
            return "studentResumeSend";
        }
        if (resume.getResumeContent() == null || "".equals(resume.getResumeContent())) {
            return "studentResumeSend";
        }
//        2.根据简历号查询该学生的所有投递记录的公司号和招聘信息号
        List<ResumeSend> resumeSendList = resumeSendRepository.findByResumeId(resume.getResumeId());
//        如果学生没投递简历到任意公司，则返回空数据
        if (resumeSendList == null || resumeSendList.isEmpty()) {
            return "studentResumeSend";
        }
//        3.对每个投递记录，查找公司名（即查找公司），职位名称、描述（即招聘信息），然后传回前端
        for (int i = 0; i < resumeSendList.size(); i++) {
//            3.1 查找公司名
            Company company = companyRepository.findByCompanyId(resumeSendList.get(i).getCompanyId());
//            3.2 查找职位
            Recruit recruit = recruitRepository.findByRecruitId(resumeSendList.get(i).getRecruitId());
            if (company == null || recruit == null) {
                System.out.println("内部查询出错：没找到投递记录所对应的公司或招聘信息");
                return "studentResumeSend";
            }
//            3.3 添加到list中
            timeList.add(resumeSendList.get(i).getDateTime().toString());
            companyNameList.add(company.getName());
            recruitList.add(recruit);
        }
//        4.将list添加到model对象里面,然后发回前端
        model.addAttribute("timeList",timeList);
        model.addAttribute("companyNameList",companyNameList);
        model.addAttribute("recruitList",recruitList);
        return "studentResumeSend";
    }

    /**
     * 请求学生收藏信息页面
     */
    @GetMapping(value = "/star")
    public String studentStar(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
            return "index";
        }
        Student student = (Student) loginUser;
        model.addAttribute("student",student);
        //定义数据结构
        List<Company> companyList = new ArrayList<>();
        List<String> functionNameList = new ArrayList<>();

        //通过学生ID获取到收藏公司的List
        List<StarCompany> starCompanyList = starCompanyRepository
                .findByStudentId(student.getStudentId());
        //通过学生ID获取到收藏标签的List
        List<StarTag> starTagList = starTagRepository
                .findByStudentId(student.getStudentId());
        //有无收藏的公司
        if (starCompanyList == null || starCompanyList.isEmpty() ||
                (starCompanyList.size() == 1 && starCompanyList.get(0) == null)) {
            System.out.println("无收藏的公司");
        } else {
            for (StarCompany tempStarCompany : starCompanyList) {
                Company company = companyRepository.findByCompanyId(tempStarCompany.getCompanyId());
                companyList.add(company);
            }
            model.addAttribute("companyList",companyList);
        }
        // 有无收藏的tag
        if (starTagList == null || starTagList.isEmpty()
                || (starTagList.size() == 1 && starTagList.get(0) == null)) {
            return "studentStar";
        } else {
            for (StarTag tempStarTag : starTagList) {
                Tag tag = tagRepository.findByTagId(tempStarTag.getTagId());
                functionNameList.add(tag.getName());
            }
            model.addAttribute("functionNameList",functionNameList);
        }
        return "studentStar";
    }

    /**
     * 学生详细信息保存操作
     */
    @PostMapping(value = "/info")
    public void StudentSaveInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("UTF-8");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
            return;
        }
        Student student = (Student) loginUser;
//        解析json
        String content = request.getParameter("content");
        System.out.println(content);
        JSONObject json = new JSONObject(content);
        JSONObject infoJson = json.getJSONObject("info");
        JSONObject studyJson = json.getJSONObject("study");
        JSONObject jobintentionJson = json.getJSONObject("jobIntention");
        String phoneNum = infoJson.get("phone").toString();
        String universityName = studyJson.get("school").toString();
        String major = studyJson.get("major").toString();
        String grade = studyJson.get("grade").toString();
        String intentionCity = jobintentionJson.get("city").toString();
        String intentionIndustry = jobintentionJson.get("industry").toString();
        String intentionFunction = jobintentionJson.get("func").toString();

        StudentDetail studentDetail = studentDetailRepository.findByStudentId(student.getStudentId());
//        1.根据学生id号查询学生详细信息，如果为空，则创建一个新的学生详细信息并添入数据库；否则取出原来的，保留学生邮箱和验证标志再更新
        if (studentDetail == null) {
            StudentDetail newStudentDetail = new StudentDetail(student.getStudentId(), null, phoneNum,
                    universityName, major, grade, intentionCity, intentionIndustry, intentionFunction, false);
            studentDetailService.addStudentDetail(newStudentDetail);
        } else {
            studentDetail.setPhoneNum(phoneNum);
            studentDetail.setUniversityName(universityName);
            studentDetail.setMajor(major);
            studentDetail.setGrade(grade);
            studentDetail.setIntentionCity(intentionCity);
            studentDetail.setIntentionIndustry(intentionIndustry);
            studentDetail.setIntentionFunction(intentionFunction);
            studentDetailService.deleteByStudentId(student.getStudentId());
            studentDetailService.addStudentDetail(studentDetail);
        }
//        删除学生所有的收藏
        starTagService.deleteStarTagByStudentId(student.getStudentId());
//        2.添加学生收藏的意向大类
        String[] tags = ParseStringUtil.parseString(intentionIndustry);
        for (int i = 0; i < tags.length; i++) {
//                2.1 先看数据库里面有没有这个标签，没有则加进去
            Tag existTag = tagRepository.findByName(tags[i]);
            if (existTag == null) {
                Tag tag = new Tag(0, tags[i]);
                tagService.addTag(tag);
            }
//                2.2 找到这个标签
            Tag justTag = tagRepository.findByName(tags[i]);
            if (justTag == null) {
                return;
            }
//                2.3 再将tag添加学生收藏标签中
            StarTag starTag = new StarTag(student.getStudentId(), justTag.getTagId());
            starTagService.addStarTag(starTag);
        }
        String result = "{\"ok\":\"true\"}";
        SendInfoUtil.render(result, "text/json", response);
    }

    /**
     * 学生收藏公司和职能操作
     */
    @PostMapping(value = "/star")
    public void studentDoStar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("UTF-8");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
            return;
        }
        Student student = (Student) loginUser;
        String companyId = request.getParameter("companyId");
        String recruitId = request.getParameter("recruitId");
//        1. 先添加收藏的公司
        StarCompany starCompany = new StarCompany(Long.parseLong(companyId), student.getStudentId());
        starCompanyService.addStarCompany(starCompany);
//        2. 再添加收藏的职能
        System.out.println(recruitId);
        List<RecruitTag> recruitTagList = recruitTagRepository.findByRecruitId(Long.parseLong(recruitId));
        if(recruitTagList != null && !recruitTagList.isEmpty()){
            for(RecruitTag recruitTag : recruitTagList){
                StarTag starTag = new StarTag(student.getStudentId(),recruitTag.getTagId());
                starTagService.addStarTag(starTag);
            }
        }
        String result = "{\"ok\":\"true\"}";
        SendInfoUtil.render(result, "text/json", response);
    }

    /**
     * 删除学生收藏的公司和职能
     */
    @DeleteMapping(value = "/star")
    public void studentDeleteStar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("UTF-8");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
            return;
        }
        Student student = (Student) loginUser;
        String companyId = request.getParameter("companyId");
        String recruitId = request.getParameter("recruitId");
//        1. 先删除收藏的公司
        JSONObject json = new JSONObject();
        Map<String, Object> starCompanyMap = new HashMap<String, Object>() {
            {
                put("studentId", student.getStudentId());
                put("companyId", Long.parseLong(companyId));
            }
        };
        starCompanyService.deleteById(starCompanyMap);
//        2. 再删除收藏的tag
        List<RecruitTag> recruitTagList = recruitTagRepository.findByRecruitId(Long.parseLong(recruitId));
        if(recruitTagList != null && !recruitTagList.isEmpty()){
            for(RecruitTag recruitTag : recruitTagList){
                Map<String, Object> starTagMap = new HashMap<String, Object>(){
                    {
                        put("studentId", student.getStudentId());
                        put("tagId", recruitTag.getTagId());
                    }
                };
                starTagService.deleteStarTagById(starTagMap);
            }
        }
        json.put("ok", "true");
        SendInfoUtil.render(json.toString(), "text/json", response);
    }

    /**
     * 删除学生收藏的公司
     */
    @DeleteMapping(value = "/star_company")
    public void studentDeleteStarCompany(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("UTF-8");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
            return;
        }
        Student student = (Student) loginUser;
        String companyId = request.getParameter("companyId");
        JSONObject json = new JSONObject();
        Map<String, Object> starCompanyMap = new HashMap<String, Object>() {
            {
                put("studentId", student.getStudentId());
                put("companyId", Long.parseLong(companyId));
            }
        };
        starCompanyService.deleteById(starCompanyMap);
        json.put("ok", "true");
        SendInfoUtil.render(json.toString(), "text/json", response);
    }

    /**
     * 删除学生收藏的职能
     */
    @DeleteMapping(value = "/star_tag")
    public void studentDeleteStarTag(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("UTF-8");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
            return;
        }
        Student student = (Student) loginUser;
        String tagName = request.getParameter("tagName");
        JSONObject json = new JSONObject();
        Tag tag = tagRepository.findByName(tagName);
        Map<String, Object> starTagMap = new HashMap<String, Object>(){
            {
                put("studentId", student.getStudentId());
                put("tagId", tag.getTagId());
            }
        };
        starTagService.deleteStarTagById(starTagMap);
        json.put("ok", "true");
        SendInfoUtil.render(json.toString(), "text/json", response);
    }

    /**
     * 学生投递简历操作
     */
    @PostMapping(value = "/resume_send")
    public void studentDoResumeSend(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("UTF-8");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
            return;
        }
        Student student = (Student) loginUser;
        String result = null;
        String recruitId = request.getParameter("jobid");
//        判断传输正误
        if (recruitId == null || recruitId.equals("")) {
            result = "{\"ok\":\"false\",\"reason\":\"传到后台的招聘信息号为空\"}";
            SendInfoUtil.render(result, "text/json", response);
            return;
        }
//        检查该学生是否已经验证邮箱
        StudentDetail studentDetail = studentDetailRepository.findByStudentId(student.getStudentId());
        if (studentDetail == null) {
            result = "{\"ok\":\"false\",\"reason\":\"你还没有验证学生身份\"}";
            SendInfoUtil.render(result, "text/json", response);
            return;
        } else if (!studentDetail.getAuthentication()) {
            result = "{\"ok\":\"false\",\"reason\":\"你还没有验证学生身份\"}";
            SendInfoUtil.render(result, "text/json", response);
            return;
        }
//        查询简历表，判断是否已经填写简历
        Resume resume = resumeRepository.findByStudentId(student.getStudentId());
        if (resume == null || resume.getResumeContent() == null || resume.getResumeContent().equals("")) {
            result = "{\"ok\":\"false\",\"reason\":\"你还没有填写简历\"}";
            SendInfoUtil.render(result, "text/json", response);
            return;
        }
//        判断是否已投过该简历
        ResumeSend resumeSend = resumeSendRepository.findByResumeIdAndRecruitId(resume.getResumeId(),Long.parseLong(recruitId));
        if(resumeSend != null){
            System.out.println("已向该职位投递过简历，请勿重复投递");
            result = "{\"ok\":\"false\",\"reason\":\"已向该职位投递过简历，请勿重复投递\"}";
            SendInfoUtil.render(result, "text/json", response);
            return;
        }
//        查询发布招聘信息的公司
        Recruit recruit = recruitRepository.findByRecruitId(Long.parseLong(recruitId));
        if (recruit == null) {
            System.out.println("内部错误:查询招聘信息出错");
            result = "{\"ok\":\"false\",\"reason\":\"我们的数据出了点差错\"}";
            SendInfoUtil.render(result, "text/json", response);
            return;
        }
        Company company = companyRepository.findByCompanyId(recruit.getCompanyId());
        if (company == null) {
            System.out.println("内部错误:查询公司出错");
            result = "{\"ok\":\"false\",\"reason\":\"我们的数据出了点差错\"}";
            SendInfoUtil.render(result, "text/json", response);
            return;
        }
        System.out.println("recruitid:" + recruitId);
        Timestamp dateTime = new Timestamp(System.currentTimeMillis());
        resumeSend = new ResumeSend(Long.parseLong("0"), resume.getResumeId(), company.getCompanyId(),
                Long.parseLong(recruitId), dateTime, false, false);
        resumeSendService.addResumeSend(resumeSend);
        result = "{\"ok\":\"true\"}";
        SendInfoUtil.render(result, "text/json", response);
    }

    /**
     * 学生进行邮箱验证
     */
    @PostMapping(value = "/authentication")
    public void studentDoAuthentication(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
            return;
        }
        Student student = (Student) loginUser;
//        1. 如果学生没未填写详细信息，则创建一个新的
        StudentDetail studentDetail = studentDetailRepository.findByStudentId(student.getStudentId());
        if(studentDetail == null){
            studentDetail = new StudentDetail(student.getStudentId(),null,null,null,
                    null,null,null,null,null,false);
            studentDetailService.addStudentDetail(studentDetail);
        }
//        2. 发送验证邮件
        List<String> list = new ArrayList<>();
        list.add("studentAuthentication");
        list.add("unUseString");
        MailUtil mailUtil = new MailUtil(list);

        String email = request.getParameter("mailbox");
        JSONObject SendStatusJson = new JSONObject();
        boolean isSuccess = true;
        System.out.println("email =" + email);
        if (email == null || "".equals(email)) {
            System.out.println("email error");
            SendStatusJson.put("ok", false);
        } else {
            try {
                mailUtil.send(email);
                isSuccess = true;
            } catch (MessagingException e) {
                e.printStackTrace();
                isSuccess = false;

            } finally {
                SendStatusJson.put("ok", isSuccess);
            }

        }
        SendInfoUtil.render(SendStatusJson.toString(), "text/json", response);
    }

    /**
     * 学生在邮箱里点击了确认链接
     */
    @GetMapping(value = "/confirm_authentication")
    public void studentConfirmAuthentication(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            return;
        }
        Student student = (Student) loginUser;
        //TODO:取出get请求参数，进行解密，排除非法验证
//        将学生验证标志改为已验证
        studentDetailService.updateStudentDetailAuthenticationByStudentId(student.getStudentId());
        String result = "Congratulations! You have validated your account successfully.";
        SendInfoUtil.render(result, "text", response);
    }

    /**
     * 保存简历
     */
    @PostMapping(value = "/resume")
    public void studentSaveResume(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
            return;
        }
        Student student = (Student) loginUser;
        Resume resume = resumeRepository.findByStudentId(student.getStudentId());
        if (resume == null){
            resume = new Resume(0L,student.getStudentId(),"");
            resumeService.addResume(resume);
        }
        String content = request.getParameter("content");
        resume.setResumeContent(content);
        resumeService.updateResume(resume);
    }


    /**
     * 加载简历
     */
    @GetMapping(value = "/request_resume")
    public void studentRequestResume(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
            return;
        }
        //String resumeid = request.getParameter("resumeid");
        Student student = (Student) loginUser;
        Resume resume = resumeRepository.findByStudentId(student.getStudentId());
        SendInfoUtil.render(resume.getResumeContent(), "text/json", response);
    }





    /**
     * 请求学生邮箱验证状态
     */
    /*@GetMapping(value = "/request_authentication")
    public void studentRequestAuthentication(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
        }
        Student student = (Student) loginUser;
        StudentDetail studentDetail = studentDetailRepository.findByStudentId(student.getStudentId());
        String result;

        if(studentDetail != null){
            Boolean studentAuthenticationState = studentDetail.getAuthentication(); // 当前学生的验证状态
            if ((studentAuthenticationState != null) && (studentAuthenticationState)) {
                System.out.println("已验证成功");
                result = "{\"authentication\":\"true\"}";
                SendInfoUtil.render(result, "text/json", response);
                return;
            }
        }

        result = "{\"authentication\":\"false\"}";
        SendInfoUtil.render(result, "text/json", response);
    }*/
    /**
     * 学生查看具体的招聘信息
     */
    /*@GetMapping(value = "/request_recruit")
    public void studentRequestRecruit(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
        }
        Student student = (Student) loginUser;
        Long recruitId;
        if (request.getParameter("id") == null) {
            System.out.println("getParameter error");
        } else {
            recruitId = Long.parseLong(request.getParameter("id"));
            JSONObject json = new JSONObject();
            JSONObject contentJson = new JSONObject();
            String haveStar = "false";
            Recruit recruit = recruitRepository.findByRecruitId(recruitId);
            Company company = companyRepository
                    .findByCompanyId(recruit.getCompanyId());


            List<StarCompany> starCompanyList = starCompanyRepository
                    .findByStudentId(student.getStudentId());
            //List空值判定
            if (starCompanyList == null || starCompanyList.isEmpty()
                    || (starCompanyList.size() == 1 && starCompanyList.get(0) == null)) {
                System.out.println("starCompanyList is error");
            } else {
                for (StarCompany starCompany : starCompanyList) {
                    if (starCompany.getCompanyId() == recruit.getCompanyId()) {
                        haveStar = "true";
                        break;
                    }
                }
            }

            //haveStarJson.put("havestar",havestar);
            //companyIdJson.put("companyid",recruit.getCompanyId());
            contentJson.put("jobName", recruit.getJobName());
            contentJson.put("name", company.getName());
            contentJson.put("location", recruit.getLocation());
            contentJson.put("lowSalary", recruit.getLowSalary());
            contentJson.put("highSalary", recruit.getHighSalary());
            contentJson.put("companyIntroduction", company.getCompanyIntroduction());
            contentJson.put("deadline", recruit.getDeadline());
            contentJson.put("jobDescribe", recruit.getJobDescribe());
            contentJson.put("jobRequire", recruit.getJobRequire());

            json.put("haveStar", haveStar);
            json.put("companyId", recruit.getCompanyId());
            json.put("content", contentJson);
            json.put("jobId", recruitId);

            System.out.println(json);
            SendInfoUtil.render(json.toString(), "text/json", response);

        }
    }*/

    /**
     * 请求学生用户详细信息
     * 若第一次进入页面则都为空
     */
    /*@GetMapping(value = "/detail_info")
    public void studentDetailInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("UTF-8");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
            return;
        }
        //        先建立好要传回前端的数据结构
        Student student = (Student) loginUser;
        JSONObject json = new JSONObject();
        JSONObject infoJson = new JSONObject();
        JSONObject studyJson = new JSONObject();
        JSONObject jobIntentionJson = new JSONObject();

        infoJson.put("mailbox", student.getMailbox());
        //通过studentId获取student中的email
        //通过studentID获取studentDetail中的其他信息
        StudentDetail studentDetail = studentDetailRepository.findByStudentId(student.getStudentId());
        if (studentDetail == null) {
            json.put("info", infoJson);
        } else {
            infoJson.put("phone", studentDetail.getPhoneNum());
            studyJson.put("school", studentDetail.getUniversityName());
            studyJson.put("major", studentDetail.getMajor());
            studyJson.put("grade", studentDetail.getGrade());
            String[] intentionCitys = ParseStringUtil.parseString(studentDetail.getIntentionCity());
            String[] intentionIndustry = ParseStringUtil.parseString(studentDetail.getIntentionIndustry());
            String[] intentionFunc = ParseStringUtil.parseString(studentDetail.getIntentionFunction());
            jobIntentionJson.put("city", intentionCitys);
            jobIntentionJson.put("industry", intentionIndustry);
            jobIntentionJson.put("func", intentionFunc);

            json.put("info", infoJson);
            json.put("study", studyJson);
            json.put("jobIntention", jobIntentionJson);
        }
        SendInfoUtil.render(json.toString(), "text/json", response);
    }*/


    /**
     * 请求收藏的公司以及收藏的标签
     */
   /* @GetMapping(value = "/request_star")
    public void StudentRequestStars(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("UTF-8");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
            return;
        }
        //定义
        Student student = (Student) loginUser;
        JSONObject json = new JSONObject();
        JSONArray companyJsonArray = new JSONArray();
        JSONArray tagJsonArray = new JSONArray();

        //通过学生ID获取到收藏公司的List
        List<StarCompany> starCompanyList = starCompanyRepository
                .findByStudentId(student.getStudentId());
        //通过学生ID获取到收藏标签的List
        List<StarTag> starTagList = starTagRepository
                .findByStudentId(student.getStudentId());
        //空值判定
        if (starCompanyList == null || starCompanyList.isEmpty() ||
                (starCompanyList.size() == 1 && starCompanyList.get(0) == null)) {
            json.put("company", companyJsonArray);
            //SendInfoUtil.render(json.toString(),"text/json",response);
            //return;

        } else {
            for (StarCompany tempStarCompany : starCompanyList) {
                JSONObject companyJson = new JSONObject();
                Company company = companyRepository
                        .findByCompanyId(tempStarCompany.getCompanyId());
                companyJson.put("starcomid", company.getCompanyId());
                companyJson.put("comname", company.getName());
                companyJson.put("email", company.getMailbox());
                companyJson.put("phonenum", company.getPhoneNum());
                companyJson.put("comdesc", company.getCompanyIntroduction());
                companyJson.put("iconaddress", company.getIconAddress());
                companyJsonArray.put(companyJson);
            }
            json.put("company", companyJsonArray);

        }

        if (starTagList == null || starTagList.isEmpty()
                || (starTagList.size() == 1 && starTagList.get(0) == null)) {
            json.put("job", tagJsonArray);
        } else {
            for (StarTag tempStarTag : starTagList) {
                JSONObject tagJson = new JSONObject();
                Tag tag = tagRepository.findByTagId(tempStarTag.getTagId());
                tagJson.put("jobTitle", tag.getName());
                tagJsonArray.put(tagJson);
            }
            json.put("job", tagJsonArray);
        }
        SendInfoUtil.render(json.toString(), "text/json", response);
    }*/

    /**
     * 请求学生投递过的招聘信息
     *//*
    @GetMapping(value = "/request_resume_send")
    public void StudentRequestResumeSend(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("UTF-8");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
            return;
        }
//        先建立好要传回前端的数据结构
        Student student = (Student) loginUser;
        JSONObject json = new JSONObject();
        JSONArray resumeSendDataJson = new JSONArray();
//        查询学生投递信息:
//        1.先找到学生对应的简历号
        Resume resume = resumeRepository.findByStudentId(student.getStudentId());
//        如果学生没写简历，则返回空数据
        if (resume == null) {
            json.put("resumeSendData", resumeSendDataJson);
            SendInfoUtil.render(json.toString(), "text/json", response);
            return;
        }
        if (resume.getResumeContent() == null || "".equals(resume.getResumeContent())) {
            json.put("resumeSendData", resumeSendDataJson);
            SendInfoUtil.render(json.toString(), "text/json", response);
            return;
        }
//        2.根据简历号查询该学生的所有投递记录的公司号和招聘信息号
        List<ResumeSend> resumeSendList = resumeSendRepository.findByResumeId(resume.getResumeId());
//        如果学生没投递简历到任意公司，则返回空数据
        if (resumeSendList == null || resumeSendList.isEmpty()) {
            json.put("resumeSendData", resumeSendDataJson);
            SendInfoUtil.render(json.toString(), "text/json", response);
            return;
        }
//        3.对每个投递记录，查找公司名（即查找公司），职位名称、描述（即招聘信息），然后传回前端
        for (int i = 0; i < resumeSendList.size(); i++) {
//            3.1 查找公司名
            Company company = companyRepository.findByCompanyId(resumeSendList.get(i).getCompanyId());
//            3.2 查找职位
            Recruit recruit = recruitRepository.findByRecruitId(resumeSendList.get(i).getRecruitId());
            if (company == null || recruit == null) {
                System.out.println("内部查询出错：没找到投递记录所对应的公司或招聘信息");
                json.put("resumeSendData", resumeSendDataJson);
                SendInfoUtil.render(json.toString(), "text/json", response);
                return;
            }
//            3.3 添加到json数组中
            JSONObject resumeSendJson = new JSONObject();
            resumeSendJson.put("time", resumeSendList.get(i).getDateTime());
            resumeSendJson.put("companyName", company.getName());
            resumeSendJson.put("jobTitle", recruit.getJobName());
            resumeSendJson.put("jobDescribe", recruit.getJobDescribe());
            resumeSendJson.put("jobHref", "/student/recruit?id=" + recruit.getRecruitId());
            resumeSendJson.put("haveDelete", recruit.getHaveDelete());
            resumeSendDataJson.put(resumeSendJson);
        }
//        4.将json数组添加到json对象里面,然后发回前端
        json.put("resumeSendData", resumeSendDataJson);
        SendInfoUtil.render(json.toString(), "text/json", response);
    }*/
}


