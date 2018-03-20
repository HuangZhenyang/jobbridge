package com.galigaigai.jobbridge.controller;

import com.galigaigai.jobbridge.model.*;
import com.galigaigai.jobbridge.repository.*;
import com.galigaigai.jobbridge.service.RecruitService;
import com.galigaigai.jobbridge.service.RecruitTagService;
import com.galigaigai.jobbridge.service.ResumeSendService;
import com.galigaigai.jobbridge.service.TagService;
import com.galigaigai.jobbridge.util.ParseStringUtil;
import com.galigaigai.jobbridge.util.SendInfoUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HanrAx on 2018/3/13 0013.
 * 公司部分的controller
 */
@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private RecruitRepository recruitRepository;
    @Autowired
    private RecruitService recruitService;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagService tagService;
    @Autowired
    private RecruitTagService recruitTagService;
    @Autowired
    private RecruitTagRepository recruitTagRepository;
    @Autowired
    private ResumeSendRepository resumeSendRepository;
    @Autowired
    private ResumeSendService resumeSendService;
    @Autowired
    private ResumeRepository resumeRepository;
    @Autowired
    private StudentRepository studentRepository;


    /**
     * @author:huangzhenyang
     * 请求公司信息页面
     */
    @GetMapping(value = "/info")
    public String showCompanyInfo(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            response.sendRedirect("/");
        }

        Company company = (Company)loginUser;
        model.addAttribute("company", company);
        return "companyInfo";
    }

    /**
     * 请求公司信息数据
     */

    /*@GetMapping(value = "/request_info")
    public void companyShowInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {

        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !(loginUser instanceof Company)){
            response.sendRedirect("/");
            return;
        }
        Company company = (Company)loginUser;
        JSONObject json = new JSONObject();
        json.put("name",company.getName());
        json.put("userName",company.getUserName());
        json.put("mailbox",company.getMailbox());
        json.put("phoneNum",company.getPhoneNum());
        json.put("companyIntroduction",company.getCompanyIntroduction());
        SendInfoUtil.render(json.toString(),"text/json",response);
    }*/
    /**
     * 请求公司编辑与发布
     * 新的招聘信息页面
     */
    @GetMapping(value = "/new_recruit")
    public String showCompanyNewRecruit(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
        }
        Company company = (Company) loginUser;
//        查找意向字典
        List<Tag> tagList = tagRepository.findAll();
//        转化为string数组
        String[] functionDictionary = new String[tagList.size()];
        for(int i = 0;i < tagList.size();i++){
            functionDictionary[i] = tagList.get(i).getName();
        }
        model.addAttribute("functionDictionary",functionDictionary);
        model.addAttribute("userName", company.getUserName());
        return "companyPublishRecruit";
    }

    /**
     * @author:huangzhenyang
     * 请求公司已发布的招聘信息页面
     */
    @GetMapping(value = "/recruit")
    public String showRecruit(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
        }

        Company company = (Company) loginUser;
        List<Recruit> recruitList = recruitRepository.findByCompanyId(company.getCompanyId()); // 所有的recruit

        // 如果没有招聘信息，直接返回html页面
        if(recruitList == null || recruitList.isEmpty() ||
                (recruitList.size() == 1 && recruitList.get(0) == null)){
            return "companyPublishedRecruit";
        }else{
            // 对于每个未删除的招聘信息，查询招聘信息号、职位名称和发布时间作为json对象添加进入json数组中
            for(int i=0; i<recruitList.size(); i++){
                if(recruitList.get(i).getHaveDelete()){ // 如果招聘信息已经删除的话, 就从recruitList中删除
                    recruitList.remove(i);
                    i--; // 索引要减1,不然会报错java.util.ConcurrentModificationException
                }
            }
            model.addAttribute("recruitList", recruitList);
        }

        //        查找意向字典
        List<Tag> tagList = tagRepository.findAll();
//        转化为string数组
        String[] functionDictionary = new String[tagList.size()];
        for(int i = 0;i < tagList.size();i++){
            functionDictionary[i] = tagList.get(i).getName();
        }
        model.addAttribute("functionDictionary",functionDictionary);
        model.addAttribute("userName", company.getUserName());

        return "companyPublishedRecruit";
    }


    /**
     * 请求公司已发布的招聘信息列表
     * */
    /*@GetMapping(value = "/request_recruit_list")
    public void requestCompanyRecruitList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if(loginUser == null || !(loginUser instanceof Company)){
            response.sendRedirect("/");
            return;
        }
        Company company = (Company) loginUser;
        //定义数据结构
        JSONObject json = new JSONObject();
        JSONArray recruitListJsonArray = new JSONArray();
//        根据公司号查询所有招聘信息
        List<Recruit> recruitList = recruitRepository.findByCompanyId(company.getCompanyId());
//        如果没有招聘信息，则返回空数据
        if(recruitList == null || recruitList.isEmpty() ||
                (recruitList.size() == 1 && recruitList.get(0) == null)){
            json.put("recruitList",recruitListJsonArray);
            SendInfoUtil.render(json.toString(),"text/json",response);
            return;
        }
//        对于每个未删除的招聘信息，查询招聘信息号、职位名称和发布时间作为json对象添加进入json数组中
        for(Recruit recruit:recruitList){
            if(recruit.getHaveDelete()){
                continue;
            }
            JSONObject recruitJson = new JSONObject();
            recruitJson.put("publishedRecruitId",recruit.getRecruitId());
            recruitJson.put("publishedRecruitTime",recruit.getDateTime().toString());
            recruitJson.put("jobTitle",recruit.getJobName());
            recruitListJsonArray.put(recruitJson);
        }
        json.put("recruitList",recruitListJsonArray);
        SendInfoUtil.render(json.toString(),"text/json",response);
    }
*/

    /**
     * 公司删除已发布的招聘信息操作
     */
    @DeleteMapping(value = "/recruit")
    public void companyDeleteRecruit(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
            return;
        }
        Company company = (Company) loginUser;
        String recruitId = request.getParameter("id");
        Recruit recruit = recruitRepository.findByRecruitId(Long.parseLong(recruitId));
        String result;
        if (!company.getCompanyId().equals(recruit.getCompanyId())) {
            result = "{\"ok\":\"false\",\"reason\":\"非法删除其他公司招聘信息\"}";
        } else {
            recruitService.updateHaveDeleteById(Long.parseLong(recruitId));
            result = "{\"ok\":\"true\"}";
        }
        SendInfoUtil.render(result, "text/json", response);
    }

    /**
     * 公司发布或修改招聘信息操作
     */
    @PostMapping(value = "/recruit")
    public void companyModifyRecruit(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
            return;
        }
        System.out.println("已经进入公司发布或修改招聘信息");
        Company company = (Company) loginUser;
        String recruitId = request.getParameter("id");
        String jobName = request.getParameter("jobName");
        String location = request.getParameter("location");
        String lowSalary = request.getParameter("lowSalary");
        String highSalary = request.getParameter("highSalary");
        String deadline = request.getParameter("deadline");
        String jobDescribe = request.getParameter("jobDescribe");
        String jobRequire = request.getParameter("jobRequire");
        String tagName = request.getParameter("industry");
        System.out.println("id:" + recruitId);
        Timestamp dateTime = new Timestamp(System.currentTimeMillis());
        String[] tags = ParseStringUtil.parseString(tagName);
//        1.如果是新发布招聘信息
        if (recruitId == null || recruitId.equals("")) {
            System.out.println("发布新招聘信息");
//            1.1 添加招聘信息
            Recruit recruit = new Recruit(Long.parseLong("0"), company.getCompanyId(), jobName, jobDescribe, jobRequire, location,
                    Integer.parseInt(lowSalary), Integer.parseInt(highSalary), dateTime, deadline, false);
            recruitService.addRecruit(recruit);
            Recruit justRecruit = recruitRepository.findLastRecruitByCompanyId(company.getCompanyId());
            if (justRecruit == null) {
                System.out.println("内部错误");
                return;
            }
//            1.2 处理标签问题
            for (int i = 0; i < tags.length; i++) {
//                1.2.1 先看数据库里面有没有这个标签，没有则加进去
                Tag existTag = tagRepository.findByName(tags[i]);
                if (existTag == null) {
                    Tag tag = new Tag(0, tags[i]);
                    tagService.addTag(tag);
                }
//                1.2.2 找到这个标签
                Tag justTag = tagRepository.findByName(tags[i]);
                if (justTag == null) {
                    return;
                }
//                1.2.3 再将tag添加到招聘信息所属标签表中
                RecruitTag recruitTag = new RecruitTag(justRecruit.getRecruitId(), justTag.getTagId());
                recruitTagService.addRecruitTag(recruitTag);
            }
            String result = "{\"ok\":\"true\", \"reason\":\"提交成功\"}";
            SendInfoUtil.render(result, "text/json", response);
//            2.如果是修改招聘信息
        } else {
            String result = null;
            Recruit recruit = recruitRepository.findByRecruitId(Long.parseLong(recruitId));
            if (recruit == null) {
                result = "{\"ok\":\"false\",\"reason\":\"你要修改的招聘信息不存在\"}";
                SendInfoUtil.render(result, "text/json", response);
                return;
            } else if (recruit.getHaveDelete()) {
                result = "{\"ok\":\"false\",\"reason\":\"你要修改的招聘信息已经被删除\"}";
                SendInfoUtil.render(result, "text/json", response);
                return;
            }
//            2.1 修改招聘信息内容
            Map<String, Object> map = new HashMap<>();
            map.put("recruitId", Long.parseLong(recruitId));
            map.put("jobName", jobName);
            map.put("jobDescribe", jobDescribe);
            map.put("jobRequire", jobRequire);
            map.put("location", location);
            map.put("lowSalary", Integer.parseInt(lowSalary));
            map.put("highSalary", Integer.parseInt(highSalary));
            map.put("deadline", deadline);
            recruitService.updateRecruitById(map);
//            2.2 删除原来的所有tag所属
            recruitTagService.deleteRecruitTagByRecruitId(Long.parseLong(recruitId));
//            2.3 对每个tag,添加新的tag所属
            for (int i = 0; i < tags.length; i++) {
//                2.2.1 找到这个标签
                Tag existTag = tagRepository.findByName(tags[i]);
                if (existTag == null) {
                    Tag tag = new Tag(0, tags[i]);
                    tagService.addTag(tag);
                }
                Tag justTag = tagRepository.findByName(tags[i]);
                if (justTag == null) {
                    System.out.println("内部错误");
                    return;
                }
//                2.2.2 再将tag添加到招聘信息所属标签表中
                RecruitTag recruitTag = new RecruitTag(Long.parseLong(recruitId), justTag.getTagId());
                recruitTagService.addRecruitTag(recruitTag);
            }
            result = "{\"ok\":\"true\", \"reason\":\"修改成功\"}";
            SendInfoUtil.render(result, "text/json", response);
        }
    }

    /**
     * 请求公司已发布的招聘信息详细
     */
    @GetMapping(value = "/request_recruit")
    public void showCompanyRecruitDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
            return;
        }
        String recruitId = request.getParameter("id");
        if (recruitId == null) {
            return;
        }
//        定义数据结构
        JSONObject json = new JSONObject();
//        根据招聘信息号查询招聘信息
        Recruit recruit = recruitRepository.findByRecruitId(Long.parseLong(recruitId));
        if (recruit == null) {
            SendInfoUtil.render(json.toString(), "text/json", response);
            return;
        }
//        将招聘信息号、职位名称和发布时间添加进入json中
        json.put("jobName", recruit.getJobName());
        json.put("location", recruit.getLocation());
        json.put("lowSalary", recruit.getLowSalary());
        json.put("highSalary", recruit.getHighSalary());
        json.put("deadline", recruit.getDeadline());
        json.put("jobDescribe", recruit.getJobDescribe());
        json.put("jobRequire", recruit.getJobRequire());
//        查询招聘信息对应的标签
        List<RecruitTag> recruitTagList = recruitTagRepository.findByRecruitId(Long.parseLong(recruitId));
        if (recruitTagList == null || recruitTagList.isEmpty() || (recruitTagList.size() == 1 && recruitTagList.get(0) == null)) {
            json.put("industry", "[]");
        } else {
            String[] industry = new String[recruitTagList.size()];
//            对于每一个标签号，查询对应的标签名
            for (int i = 0; i < recruitTagList.size(); i++) {
                Tag tag = tagRepository.findByTagId(recruitTagList.get(i).getTagId());
                if (tag != null) {
                    industry[i] = tag.getName();
                }
            }
            json.put("industry", industry);
        }
        SendInfoUtil.render(json.toString(), "text/json", response);
    }


    /**
     * @author:huangzhenyang
     * 请求公司查看投递信息页面
     */
    @GetMapping(value = "/resume_received")
    public String showCompanyReceivedResume(HttpServletRequest request, HttpServletResponse response,
                                            Model model) throws IOException {

        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
        }

        Company company = (Company) loginUser;
        //根据公司号查询所有招聘信息
        List<ResumeSend> resumeSendList = resumeSendRepository
                .findByCompanyId(company.getCompanyId());
        List<String> recruitTitleList = new ArrayList<>();
        List<String> studentNameList = new ArrayList<>();
        List<String> readStatusList = new ArrayList<>();

        //空值判定
        if (resumeSendList == null || resumeSendList.isEmpty() ||
                (resumeSendList.size() == 1 && resumeSendList.get(0) == null)) {
            //json.put("resumeSendList", resumeSendListJsonArray);
            return "companyReceivedResume";
        } else {
            for (ResumeSend resumeSend : resumeSendList) {
                if (resumeSend.getHaveDelete()) {
                    resumeSendList.remove(resumeSend);
                } else {
                    //此处Json对应companyReceiveResume.js处json
//                    JSONObject resumeSendJson = new JSONObject();
//                    resumeSendJson.put("resumeSendId", resumeSend.getResumeId());
//                    resumeSendJson.put("resumeSendTime", resumeSend.getDateTime());
                    Recruit recruit = recruitRepository.findByRecruitId(resumeSend.getRecruitId());
                    recruitTitleList.add(recruit.getJobName());
                    //username
                    Resume resume = resumeRepository.findByResumeId(resumeSend.getResumeId());
                    Student student = studentRepository.findByStudentId(resume.getStudentId());
                    // resumeSendJson.put("studentUserName", student.getUserName());
                    studentNameList.add(student.getUserName());
                    //status
                    if (resumeSend.getHaveRead()) {
                        readStatusList.add("已读");
                    } else {
                        readStatusList.add("未读");
                    }
                    // TODO: 暂定返回三个数组,resumeSendList,studentNameList以及readStatusList;
                    // 前端遍历resumeSendList,通过th:each="obj,iterStat:${objList}"
                    // th:each="user,userStat : ${list}"  的${userStat.index}的到下标,再去拿到其他两个数组的对应位置的数据
                    //resumeSendListJsonArray.put(resumeSendJson);

                }
            }

            if(resumeSendList != null){
                model.addAttribute("resumeSendList", resumeSendList);
            }

            if(recruitTitleList != null){
                model.addAttribute("recruitTitleList", recruitTitleList);
            }

            if(studentNameList != null){
                model.addAttribute("studentNameList", studentNameList);
            }

            if(readStatusList != null){
                model.addAttribute("readStatusList", readStatusList);
            }


        }

        model.addAttribute("userName", company.getUserName());
        return "companyReceivedResume";
    }

    /**
     * 请求公司收到的投递信息
     */
    /*@GetMapping(value = "request_resume_received")
    public void companyForDeliverInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
            return;
        }
        Company company = (Company) loginUser;
        System.out.println("已经进入公司投递信息显示操作");
        //定义数据结构
        JSONObject json = new JSONObject();
        JSONArray resumeSendListJsonArray = new JSONArray();
//        根据公司号查询所有招聘信息
        List<ResumeSend> resumeSendList = resumeSendRepository
                .findByCompanyId(company.getCompanyId());
        //空值判定
        if (resumeSendList == null || resumeSendList.isEmpty() ||
                (resumeSendList.size() == 1 && resumeSendList.get(0) == null)) {
            json.put("resumeSendList", resumeSendListJsonArray);
            //SendInfoUtil.render(json.toString(),"text/json",response);
        } else {
            for (ResumeSend resumeSend : resumeSendList) {
                if (resumeSend.getHaveDelete()) {
                    continue;
                } else {
                    //此处Json对应companyReceiveResume.js处json
                    JSONObject resumeSendJson = new JSONObject();
                    resumeSendJson.put("resumeSendId", resumeSend.getResumeSendId());
                    resumeSendJson.put("resumeSendTime", resumeSend.getDateTime());
                    Recruit recruit = recruitRepository.findByRecruitId(resumeSend.getRecruitId());
                    resumeSendJson.put("jobTitle", recruit.getJobName());
                    //username
                    Resume resume = resumeRepository.findByResumeId(resumeSend.getResumeId());
                    Student student = studentRepository.findByStudentId(resume.getStudentId());
                    resumeSendJson.put("studentUserName", student.getUserName());
                    //status
                    if (resumeSend.getHaveRead()) {
                        resumeSendJson.put("readStatus", "已读");
                    } else {
                        resumeSendJson.put("readStatus", "未读");
                    }

                    resumeSendListJsonArray.put(resumeSendJson);

                }
            }
            json.put("resumeSendList", resumeSendListJsonArray);
        }
        System.out.println(json);
        SendInfoUtil.render(json.toString(), "text/json", response);
    }*/

    /**
     * 公司删除学生投递信息操作
     */
    @DeleteMapping(value = "/resume_received")
    public void companyDeleteReceivedResume(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
            return;
        }
        Company company = (Company)loginUser;
        String resumeSendId = request.getParameter("id");
        ResumeSend resumeSend = resumeSendRepository.findByResumeSendId(Long.parseLong(resumeSendId));
        String result;
        if (!company.getCompanyId().equals(resumeSend.getCompanyId())) {
            result = "{\"ok\":\"false\",\"reason\":\"非法删除其他公司收到的信息\"}";
        } else {
            resumeSendService.updateHaveDeleteByResumeSendId(Long.parseLong(resumeSendId));
            result = "{\"ok\":\"true\"}";
        }
        SendInfoUtil.render(result, "text/json", response);
    }

    /**
     * 公司请求投递信息对应的简历信息
     */
    @PostMapping(value = "/resume_received/resume")
    public void showCompanyReceivedResumeDetailData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
            return;
        }
        Company company = (Company)loginUser;
        System.out.println("公司请求投递信息对应得简历信息");
        String resumeSendStr = request.getParameter("resumeSendId");
        if (resumeSendStr == null || resumeSendStr == "") {
            System.out.println("resumeSendStr is null");
        }
        Long resumeSendId = Long.parseLong(resumeSendStr);
        ResumeSend resumeSend = resumeSendRepository.findByResumeSendId(resumeSendId);
        //进入页面请求简历信息时，说明该公司已经看过该简历，所以修改HaveRead标记为true
        resumeSendService.updateHaveReadByResumeSendId(resumeSendId);

        Long resumeId = resumeSend.getResumeId();
//        查找简历
        Resume resume = resumeRepository.findByResumeId(resumeId);
        SendInfoUtil.render(resume.getResumeContent(), "text/json", response);

    }

    /**
     * 请求投递信息具体简历页面
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/resume_received/resume")
    public String showCompanyReceivedResumeDetail(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
        }
        // 得到学生简历的id
        String resumeSendId = request.getParameter("id");
        model.addAttribute("resumeSendId", resumeSendId);

        Company company = (Company) loginUser;
        model.addAttribute("userName", company.getUserName());

        return "companyReceivedResumeDetail";
    }
}
