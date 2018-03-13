package com.galigaigai.jobbridge.controller;

import com.galigaigai.jobbridge.model.*;
import com.galigaigai.jobbridge.model.Tag;
import com.galigaigai.jobbridge.repository.*;
import com.galigaigai.jobbridge.service.*;
import com.galigaigai.jobbridge.util.*;
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

    /**
     * 请求学生简历页面
     * */
    @GetMapping(value = "/resume")
    public String studentResume(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !(loginUser instanceof Student)){
            response.sendRedirect("/");
        }
        return "resume";
    }

    /**
     * 学生请求具体招聘信息页面
     * */
    @GetMapping(value = "/recruit")
    public String studentRecruit(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !(loginUser instanceof Student)){
            response.sendRedirect("/");
        }
        String id = request.getParameter("id");
        model.addAttribute("id", id);
        return "studentrecruit";
    }

    /**
     * 学生请求验证页面
     * */
    @GetMapping(value = "/authentication")
    public String studentAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !(loginUser instanceof Student)){
            response.sendRedirect("/");
        }
        return "studentauthentication";
    }

    /**
    * 请求学生信息页面
    * */
    @GetMapping(value = "/info")
    public String studentInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !(loginUser instanceof Student)){
            response.sendRedirect("/");
        }
        return "studentinfo";
    }

    /**
    * 请求学生投递信息页面
    * */
    @GetMapping(value = "/resume_send")
    public String studentResumeSend(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !(loginUser instanceof Student)){
            response.sendRedirect("/");
        }
        return "studentresumesend";
    }

    /**
     * 请求学生收藏信息页面
     * */
    @GetMapping(value = "/star")
    public String studentStar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !(loginUser instanceof Student)){
            response.sendRedirect("/");
        }
        return "studentstar";
    }

    /**
    * 学生详细信息保存操作
    * */
    @PostMapping(value = "info")
    public void StudentSaveInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("UTF-8");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !(loginUser instanceof Student)){
            response.sendRedirect("/");
            return;
        }
        Student student = (Student)loginUser;
//        解析json
        String content = request.getParameter("content");
        System.out.println(content);
        JSONObject json = new JSONObject(content);
        JSONObject infoJson = json.getJSONObject("info");
        JSONObject studyJson = json.getJSONObject("study");
        JSONObject jobintentionJson = json.getJSONObject("jobintention");
        String phoneNum = infoJson.get("phone").toString();
        String universityName = studyJson.get("school").toString();
        String major = studyJson.get("major").toString();
        String grade = studyJson.get("grade").toString();
        String intentionCity = jobintentionJson.get("city").toString();
        String intentionIndustry = jobintentionJson.get("industry").toString();
        String intentionFunction = jobintentionJson.get("func").toString();

        StudentDetail studentDetail = studentDetailRepository.findByStudentId(student.getStudentId());
//        1.根据学生id号查询学生详细信息，如果为空，则创建一个新的学生详细信息并添入数据库；否则取出原来的，保留简历号和验证标志再更新
        if(studentDetail == null){
            StudentDetail newStudentDetail = new StudentDetail(student.getStudentId(),null,phoneNum,
                    universityName,major,grade,intentionCity,intentionIndustry,intentionFunction,false);
            studentDetailService.addStudentDetail(newStudentDetail);
        }else{
            studentDetail.setPhoneNum(phoneNum);
            studentDetail.setUniversityName(universityName);
            studentDetail.setMajor(major);
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
        for(int i = 0;i<tags.length;i++){
//                2.1 先看数据库里面有没有这个标签，没有则加进去
            Tag existTag = tagRepository.findByName(tags[i]);
            if(existTag == null){
                Tag tag = new Tag(0,tags[i]);
                tagService.addTag(tag);
            }
//                2.2 找到这个标签
            Tag justTag = tagRepository.findByName(tags[i]);
            if(justTag == null){
                return;
            }
//                2.3 再将tag添加学生收藏标签中
            StarTag starTag = new StarTag(student.getStudentId(),justTag.getTagId());
            starTagService.addStarTag(starTag);
        }
        String result = "{\"ok\":\"true\"}";
        SendInfoUtil.render(result,"text/json",response);
    }

    /**
    * 请求学生投递过的招聘信息
    * */
    @GetMapping(value = "/request_resume_send")
    public void StudentRequestResumeSend(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("UTF-8");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !(loginUser instanceof Student)){
            response.sendRedirect("/");
            return;
        }
//        先建立好要传回前端的数据结构
        Student student = (Student)loginUser;
        JSONObject json = new JSONObject();
        JSONArray resumeSendDataJson = new JSONArray();
//        查询学生投递信息:
//        1.先找到学生对应的简历号
        Resume resume = resumeRepository.findByStudentId(student.getStudentId());
//        如果学生没写简历，则返回空数据
        if(resume.getResumeContent() == null || resume.getResumeContent().equals("")){
            json.put("resumeSenddata",resumeSendDataJson);
            SendInfoUtil.render(json.toString(),"text/json",response);
            return;
        }
//        2.根据简历号查询该学生的所有投递记录的公司号和招聘信息号
        List<ResumeSend> deliverList = resumeSendRepository.findByResumeId(resume.getResumeId());
//        如果学生没投递简历到任意公司，则返回空数据
        if(deliverList == null || deliverList.isEmpty()){
            json.put("resumeSenddata",resumeSendDataJson);
            SendInfoUtil.render(json.toString(),"text/json",response);
            return;
        }
//        3.对每个投递记录，查找公司名（即查找公司），职位名称、描述（即招聘信息），然后传回前端
        for(int i = 0;i<deliverList.size();i++){
//            3.1 查找公司名
            Company company = companyRepository.findByCompanyId(deliverList.get(i).getCompanyId());
//            3.2 查找职位
            Recruit recruit = recruitRepository.findByRecruitId(deliverList.get(i).getRecruitId());
            if(company == null || recruit == null){
                System.out.println("内部查询出错：没找到投递记录所对应的公司或招聘信息");
                json.put("resumeSenddata",resumeSendDataJson);
                SendInfoUtil.render(json.toString(),"text/json",response);
                return;
            }
//            3.3 添加到json数组中
            JSONObject resumeSendJson = new JSONObject();
            resumeSendJson.put("time",deliverList.get(i).getDateTime());
            resumeSendJson.put("comname",company.getName());
            resumeSendJson.put("jobtitle",recruit.getJobName());
            resumeSendJson.put("jobdesc",recruit.getJobDescribe());
            resumeSendJson.put("jobhref","../../static/jobinfo.html?id=" + recruit.getRecruitId());
            resumeSendJson.put("havedel",recruit.getHaveDelete());
            resumeSendDataJson.put(resumeSendJson);
        }
//        4.将json数组添加到json对象里面,然后发回前端
        json.put("resumeSenddata",resumeSendDataJson);
        SendInfoUtil.render(json.toString(),"text/json",response);
    }

    /**
     * 学生收藏公司操作
     */
    @PostMapping(value = "/star")
    public void studentDoStar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("UTF-8");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !(loginUser instanceof Student)){
            response.sendRedirect("/");
            return;
        }
        Student student = (Student)loginUser;
        String companyId = request.getParameter("companyid");
        StarCompany starCompany = new StarCompany(Long.parseLong(companyId),student.getStudentId());
        starCompanyService.addStarCompany(starCompany);
        String result = "{\"ok\":\"true\"}";
        SendInfoUtil.render(result,"text/json",response);
    }

    /**
     * 请求收藏的公司以及收藏的标签
     */
    @GetMapping(value = "/request_star")
    public void StudentRequestStars(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("UTF-8");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !(loginUser instanceof Student)){
            response.sendRedirect("/");
            return;
        }
        //定义
        Student student = (Student)loginUser;
        JSONObject json = new JSONObject();
        JSONArray companyJsonArray = new JSONArray();
        JSONArray tagJsonArray =new JSONArray();

        //通过学生ID获取到收藏公司的List
        List<StarCompany> starCompanyList = starCompanyRepository
                .findByStudentId(student.getStudentId());
        //通过学生ID获取到收藏标签的List
        List<StarTag> starTagList = starTagRepository
                .findByStudentId(student.getStudentId());
        //空值判定
        if(starCompanyList == null || starCompanyList.isEmpty() ||
                (starCompanyList.size() == 1 && starCompanyList.get(0) == null)){
            json.put("company",companyJsonArray);
            //SendInfoUtil.render(json.toString(),"text/json",response);
            //return;

        }else{
            for (StarCompany tempStarCompany : starCompanyList) {
                JSONObject companyJson = new JSONObject();
                Company company = companyRepository
                        .findByCompanyId(tempStarCompany.getCompanyId());
                companyJson.put("starcomid",company.getCompanyId());
                companyJson.put("comname",company.getName());
                companyJson.put("email",company.getMailbox());
                companyJson.put("phonenum",company.getPhoneNum());
                companyJson.put("comdesc",company.getCompanyIntroduction());
                companyJson.put("iconaddress",company.getIconAddress());
                companyJsonArray.put(companyJson);
            }
            json.put("company",companyJsonArray);

        }

        if(starTagList == null || starTagList.isEmpty()
                || (starTagList.size() == 1 && starTagList.get(0) == null)){
            json.put("job",tagJsonArray);
        }else{
            for (StarTag tempStarTag : starTagList){
                JSONObject tagJson = new JSONObject();
                Tag tag = tagRepository.findByTagId(tempStarTag.getTagId());
                tagJson.put("jobtitle",tag.getName());
                tagJsonArray.put(tagJson);
            }
            json.put("job",tagJsonArray);
        }
        SendInfoUtil.render(json.toString(),"text/json",response);
    }

    /**
     * 请求学生用户详细信息
     * 若第一次进入页面则都为空
     */
    @GetMapping(value = "/detail_info")
    public void studentDetailInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("UTF-8");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !(loginUser instanceof Student)){
            response.sendRedirect("/");
            return;
        }
        //        先建立好要传回前端的数据结构
        Student student = (Student)loginUser;
        JSONObject json = new JSONObject();
        JSONObject infoJson = new JSONObject();
        JSONObject studyJson = new JSONObject();
        JSONObject jobIntentionJson = new JSONObject();

        infoJson.put("email",student.getMailbox());
        //通过studentId获取student中的email
        //通过studentID获取studentDetail中的其他信息
        StudentDetail studentDetail = studentDetailRepository.findByStudentId(student.getStudentId());
        if (studentDetail == null){
            json.put("info",infoJson);
        }else{
            infoJson.put("phone",studentDetail.getPhoneNum());
            studyJson.put("school",studentDetail.getUniversityName());
            studyJson.put("major",studentDetail.getMajor());
            studyJson.put("grade",studentDetail.getGrade());
            String[] intentionCitys = ParseStringUtil.parseString(studentDetail.getIntentionCity());
            String[] intentionIndustry = ParseStringUtil.parseString(studentDetail.getIntentionIndustry());
            String[] intentionFunc = ParseStringUtil.parseString(studentDetail.getIntentionFunction());
            jobIntentionJson.put("city",intentionCitys);
            jobIntentionJson.put("industry",intentionIndustry);
            jobIntentionJson.put("func",intentionFunc);

            json.put("info",infoJson);
            json.put("study",studyJson);
            json.put("jobintention",jobIntentionJson);
        }
        SendInfoUtil.render(json.toString(),"text/json",response);
    }

    /**
     * 学生查看具体的招聘信息
     */
    @GetMapping(value = "/request_recruit")
    public void studentRequestRecruit(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
        }
        Student student = (Student) loginUser;
        Long recruitId;
        if (request.getParameter("id") == null){
            System.out.println("getParameter error");
        }else{
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
                    ||(starCompanyList.size()==1 && starCompanyList.get(0)==null)){
                System.out.println("starCompanyList is error");
            }else {
                for (StarCompany starCompany : starCompanyList){
                    if (starCompany.getCompanyId() == recruit.getCompanyId()){
                        haveStar = "true";
                        break;
                    }
                }
            }

            //haveStarJson.put("havestar",havestar);
            //companyIdJson.put("companyid",recruit.getCompanyId());
            contentJson.put("jobName",recruit.getJobName());
            contentJson.put("name",company.getName());
            contentJson.put("location",recruit.getLocation());
            contentJson.put("lowSalary",recruit.getLowSalary());
            contentJson.put("highSalary",recruit.getHighSalary());
            contentJson.put("companyIntroduction",company.getCompanyIntroduction());
            contentJson.put("deadline",recruit.getDeadline());
            contentJson.put("jobDescribe",recruit.getJobDescribe());
            contentJson.put("jobRequire",recruit.getJobRequire());

            json.put("havestar",haveStar);
            json.put("companyid",recruit.getCompanyId());
            json.put("content",contentJson);
            json.put("jobid",recruitId);

            System.out.println(json);
            SendInfoUtil.render(json.toString(),"text/json",response);

        }
    }

    /**
     * 删除学生收藏的公司
     */
    @DeleteMapping(value = "/star")
    public void studentDeleteStar(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("UTF-8");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !(loginUser instanceof Student)){
            response.sendRedirect("/");
            return;
        }
        Student student = (Student)loginUser;
        String companyId = request.getParameter("id");
        JSONObject json = new JSONObject();
        Map<String,Object> map = new HashMap<String,Object>(){
            {
                put("studentId",student.getStudentId());
                put("companyId",Long.parseLong(companyId));
            }
        };
        starCompanyService.deleteById(map);
        json.put("ok","true");
        SendInfoUtil.render(json.toString(),"text/json",response);
    }

    /**
     * 学生投递简历操作
     */
    @PostMapping(value = "/resume_send")
    public void studentDoResumeSend(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        request.setCharacterEncoding("UTF-8");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !(loginUser instanceof Student)){
            response.sendRedirect("/");
            return;
        }
        Student student = (Student)loginUser;
        String result = null;
        String recruitId = request.getParameter("jobid");
//        判断传输正误
        if(recruitId == null || recruitId.equals("")){
            result = "{\"ok\":\"false\",\"reason\":\"传到后台的招聘信息号为空\"}";
            SendInfoUtil.render(result,"text/json",response);
            return;
        }
//        检查该学生是否已经验证邮箱
        StudentDetail studentDetail = studentDetailRepository.findByStudentId(student.getStudentId());
        if(studentDetail == null){
            result = "{\"ok\":\"false\",\"reason\":\"你还没有验证学生身份\"}";
            SendInfoUtil.render(result,"text/json",response);
            return;
        }else if(!studentDetail.getAuthentication()){
            result = "{\"ok\":\"false\",\"reason\":\"你还没有验证学生身份\"}";
            SendInfoUtil.render(result,"text/json",response);
            return;
    }
//        查询简历表，判断是否已经填写简历
        Resume resume = resumeRepository.findByStudentId(student.getStudentId());
        if(resume.getResumeContent() == null || resume.getResumeContent().equals("")){
            result = "{\"ok\":\"false\",\"reason\":\"你还没有填写简历\"}";
            SendInfoUtil.render(result,"text/json",response);
            return;
        }
//        查询发布招聘信息的公司
        Recruit recruit = recruitRepository.findByRecruitId(Long.parseLong(recruitId));
        if(recruit == null){
            System.out.println("内部错误:查询招聘信息出错");
            result = "{\"ok\":\"false\",\"reason\":\"我们的数据出了点差错\"}";
            SendInfoUtil.render(result,"text/json",response);
            return;
        }
        Company company = companyRepository.findByCompanyId(recruit.getCompanyId());
        if(company == null){
            System.out.println("内部错误:查询公司出错");
            result = "{\"ok\":\"false\",\"reason\":\"我们的数据出了点差错\"}";
            SendInfoUtil.render(result,"text/json",response);
            return;
        }
        System.out.println("recruitid:" + recruitId);
        Timestamp dateTime = new Timestamp(System.currentTimeMillis());
        ResumeSend deliver = new ResumeSend(Long.parseLong("0"),resume.getResumeId(),company.getCompanyId(),
                Long.parseLong(recruitId),dateTime,false,false);
        resumeSendService.addResumeSend(deliver);
        result = "{\"ok\":\"true\"}";
        SendInfoUtil.render(result,"text/json",response);
    }

    /**
     * 学生进行邮箱验证
     * */
    @PostMapping(value = "/authentication")
    public void studentDoAuthentication(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !(loginUser instanceof Student)){
            response.sendRedirect("/");
        }
        MailUtil mailUtil = new MailUtil();
        String email = request.getParameter("stuemail");
        JSONObject SendStatusJson = new JSONObject();
        boolean isSuccess = true;
        System.out.println("email ="+email);
        if (email == null ||"".equals(email)){
            System.out.println("email error");
            SendStatusJson.put("ok",false);
        }else{
            try {
                mailUtil.Send(email);
                isSuccess = true;
            } catch (MessagingException e) {
                e.printStackTrace();
                isSuccess = false;

            }finally {
                SendStatusJson.put("ok",isSuccess);
            }

        }
        SendInfoUtil.render(SendStatusJson.toString(),"text/json",response);
    }

    /**
     * 请求学生邮箱验证状态
     * */
    @GetMapping(value = "/request_authentication")
    public void studentRequestAuthentication(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
        }
        Student student = (Student) loginUser;
        StudentDetail studentDetail = studentDetailRepository.findByStudentId(student.getStudentId());
        String result;
        if (studentDetail.getAuthentication()) {
            System.out.println("已验证成功");
            result = "{\"authentication\":\"true\"}";
            SendInfoUtil.render(result, "text/json", response);
            return;
        }
        result = "{\"authentication\":\"false\"}";
        SendInfoUtil.render(result, "text/json", response);
    }

    /**
     * 学生在邮箱里点击了确认链接
     * */
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
        SendInfoUtil.render(result,"text",response);
    }

    /**
     * 保存简历
     */
    @PostMapping(value = "/resume")
    public void studentSaveResume(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
        }
        Student student = (Student) loginUser;
        Resume resume = resumeRepository.findByStudentId(student.getStudentId());
        String content = request.getParameter("content");
        resume.setResumeContent(content);
        resumeService.updateResume(resume);
    }

    /**
     * 加载简历
     */
    @GetMapping(value = "/request_resume")
    public void studentRequestResume(HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Student)) {
            response.sendRedirect("/");
        }
        //String resumeid = request.getParameter("resumeid");
        Student student = (Student) loginUser;
        Resume resume = resumeRepository.findByStudentId(student.getStudentId());
        SendInfoUtil.render(resume.getResumeContent(),"text/json",response);
    }
}
