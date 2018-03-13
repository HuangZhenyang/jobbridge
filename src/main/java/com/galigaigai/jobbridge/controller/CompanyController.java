package com.galigaigai.jobbridge.controller;

import com.galigaigai.jobbridge.model.*;
import com.galigaigai.jobbridge.util.ParseStringUtil;
import com.galigaigai.jobbridge.util.SendInfo;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HanrAx on 2018/3/13 0013.
 */
@Controller
@RequestMapping("/company")
public class CompanyController {


    /**
     * 请求公司信息页面
     */
    @GetMapping(value = "/info")
    public String showCompanyInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
        }
        return "companyInfo";
    }

    /**
     * 公司企业退出操作
     */
    @GetMapping(value = "/sign_out")
    public String doCompanyExit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
        }
        request.getSession().removeAttribute("loginUser");
        return "index";
    }

    /**
     * 请求公司编辑与发布
     * 新的招聘信息页面
     */
    @GetMapping(value = "/new_recruit")
    public String showCompanyNewRecruit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
        }
        return "companyPublishedJob";
    }

    /**
     * 请求公司已发布的招聘信息页面
     */
    @GetMapping(value = "/recruit")
    public String showRecruitInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
        }
        return "companyPublishedRecruit";
    }

    /**
     * 公司删除已发布的招聘信息操作
     */
    @DeleteMapping(value = "/recruit")
    public void companyDeleteRecruitInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
            return;
        }
        Company company = (Company) loginUser;
        String recruitInfoId = request.getParameter("id");
        RecruitInfo recruitInfo = recruitInfoRepository.findByRecruitInfoId(Long.parseLong(recruitInfoId));
        String result;
        if (!company.getCompanyId().equals(recruitInfo.getCompanyId())) {
            result = "{\"ok\":\"false\",\"reason\":\"非法删除其他公司招聘信息\"}";
        } else {
            recruitInfoService.updateHaveDeleteById(Long.parseLong(recruitInfoId));
            result = "{\"ok\":\"true\"}";
        }
        SendInfo.render(result, "text/json", response);
    }

    /**
     * 公司发布或修改招聘信息操作
     */
    @PostMapping(value = "/recruit")
    public void companyModifyRecruitInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
            return;
        }
        System.out.println("已经进入公司发布或修改招聘信息");
        Company company = (Company) loginUser;
        String recruitInfoId = request.getParameter("id");
        String jobName = request.getParameter("jobName");
        String location = request.getParameter("location");
        String lowSalary = request.getParameter("lowSalary");
        String highSalary = request.getParameter("highSalary");
        String deadline = request.getParameter("deadline");
        String jobDescribe = request.getParameter("jobDescribe");
        String jobRequire = request.getParameter("jobRequire");
        String tagName = request.getParameter("industry");
        System.out.println("id:" + recruitInfoId);
        Timestamp dateTime = new Timestamp(System.currentTimeMillis());
        String[] tags = ParseStringUtil.parseString(tagName);
//        1.如果是新发布招聘信息
        if (recruitInfoId == null || recruitInfoId.equals("")) {
            System.out.println("发布新招聘信息");
//            1.1 添加招聘信息
            RecruitInfo recruitInfo = new RecruitInfo(Long.parseLong("0"), enterprise.getEnterpriseId(), jobName, jobDescribe, jobRequire, location,
                    Integer.parseInt(lowSalary), Integer.parseInt(highSalary), dateTime, deadline, false);
            recruitInfoService.addRecruitInfo(recruitInfo);
            RecruitInfo justRecruitInfo = recruitInfoRepository.findLastRecruitInfoByEnterpriseId(enterprise.getEnterpriseId());
            if (justRecruitInfo == null) {
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
                RecruitInfoTag recruitInfoTag = new RecruitInfoTag(justRecruitInfo.getRecruitInfoId(), justTag.getTagId());
                recruitInfoTagService.addRecruitInfoTag(recruitInfoTag);
            }
            String result = "{\"ok\":\"true\"}";
            SendInfo.render(result, "text/json", response);
//            2.如果是修改招聘信息
        } else {
            String result = null;
            RecruitInfo recruitInfo = recruitInfoRepository.findByRecruitInfoId(Long.parseLong(recruitInfoId));
            if (recruitInfo == null) {
                result = "{\"ok\":\"false\",\"reason\":\"你要修改的招聘信息不存在\"}";
                SendInfo.render(result, "text/json", response);
                return;
            } else if (recruitInfo.getHaveDelete()) {
                result = "{\"ok\":\"false\",\"reason\":\"你要修改的招聘信息已经被删除\"}";
                SendInfo.render(result, "text/json", response);
                return;
            }
//            2.1 修改招聘信息内容
            Map<String, Object> map = new HashMap<>();
            map.put("recruitInfoId", Long.parseLong(recruitInfoId));
            map.put("jobName", jobName);
            map.put("jobDescribe", jobDescribe);
            map.put("jobRequire", jobRequire);
            map.put("location", location);
            map.put("lowSalary", Integer.parseInt(lowSalary));
            map.put("highSalary", Integer.parseInt(highSalary));
            map.put("deadline", deadline);
            recruitInfoService.updateRecruitInfoById(map);
//            2.2 删除原来的所有tag所属
            recruitInfoTagService.deleteRecruitInfoTagByRecruitInfoId(Long.parseLong(recruitInfoId));
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
                RecruitInfoTag recruitInfoTag = new RecruitInfoTag(Long.parseLong(recruitInfoId), justTag.getTagId());
                recruitInfoTagService.addRecruitInfoTag(recruitInfoTag);
            }
            result = "{\"ok\":\"true\"}";
            SendInfo.render(result, "text/json", response);
        }
    }

    /**
     * 请求公司已发布的招聘信息详细
     */
    @GetMapping(value = "/recruit_info")
    public void showCompanyRecruitInfoDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
            return;
        }
        String recruitInfoId = request.getParameter("id");
        if (recruitInfoId == null) {
            return;
        }
//        定义数据结构
        JSONObject json = new JSONObject();
//        根据招聘信息号查询招聘信息
        RecruitInfo recruitInfo = recruitInfoRepository.findByRecruitInfoId(Long.parseLong(recruitInfoId));
        if (recruitInfo == null) {
            SendInfo.render(json.toString(), "text/json", response);
            return;
        }
//        将招聘信息号、职位名称和发布时间添加进入json中
        json.put("jobName", recruitInfo.getJobName());
        json.put("location", recruitInfo.getLocation());
        json.put("lowSalary", recruitInfo.getLowSalary());
        json.put("highSalary", recruitInfo.getHighSalary());
        json.put("deadline", recruitInfo.getDeadline());
        json.put("jobDescribe", recruitInfo.getJobDescribe());
        json.put("jobRequire", recruitInfo.getJobRequire());
//        查询招聘信息对应的标签
        List<RecruitInfoTag> recruitInfoTagList = recruitInfoTagRepository.findByRecruitInfoId(Long.parseLong(recruitInfoId));
        if (recruitInfoTagList == null || recruitInfoTagList.isEmpty() || (recruitInfoTagList.size() == 1 && recruitInfoTagList.get(0) == null)) {
            json.put("industry", "[]");
        } else {
            String[] industry = new String[recruitInfoTagList.size()];
//            对于每一个标签号，查询对应的标签名
            for (int i = 0; i < recruitInfoTagList.size(); i++) {
                Tag tag = tagRepository.findByTagId(recruitInfoTagList.get(i).getTagId());
                if (tag != null) {
                    industry[i] = tag.getName();
                }
            }
            json.put("industry", industry);
        }
        SendInfo.render(json.toString(), "text/json", response);
    }

    /**
     * 请求公司查看投递信息页面
     */
    @GetMapping(value = "/resume_received")
    public String showCompanyReceivedResume(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
        }
        return "commessage";
    }

//    /**
//     * 请求公司收到的投递信息
//     */
//    @RequestMapping(value = "/enterprise/showdeliveryinfo", method = RequestMethod.GET)
//    public void enterpriseForDeliverInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        Object loginUser = request.getSession().getAttribute("loginUser");
//        if (loginUser == null || !(loginUser instanceof Company)) {
//            response.sendRedirect("/");
//            return;
//        }
//        Company company = (Company) loginUser;
//        System.out.println("已经进入公司投递信息显示操作");
//        //定义数据结构
//        JSONObject json = new JSONObject();
//        JSONArray dataJsonArray = new JSONArray();
////        根据公司号查询所有招聘信息
//        List<ResumeSend> deliverList = deliverRepository
//                .findByEnterpriseId(company.getCompanyId()eId());
//        //空值判定
//        if (deliverList == null || deliverList.isEmpty() ||
//                (deliverList.size() == 1 && deliverList.get(0) == null)) {
//            json.put("data", dataJsonArray);
//            //SendInfo.render(json.toString(),"text/json",response);
//        } else {
//            for (Deliver tempDeliver : deliverList) {
//                if (tempDeliver.getHaveDelete()) {
//                    continue;
//                } else {
//                    JSONObject dataJson = new JSONObject();
//                    //deliveryid
//                    dataJson.put("deliveryid", tempDeliver.getDeliverId());
//                    //deliverytime
//                    dataJson.put("deliverytime", tempDeliver.getDateTime());
//                    //jobtitle
//                    RecruitInfo recruitInfo = recruitInfoRepository
//                            .findByRecruitInfoId(tempDeliver.getRecruitInfoId());
//                    dataJson.put("jobtitle", recruitInfo.getJobName());
//                    //username
//                    Resume resume = resumeRepository
//                            .findByResumeId(tempDeliver.getResumeId());
//                    Student student = studentRepository
//                            .findByStudentId(resume.getStudentId());
//                    dataJson.put("username", student.getUserName());
//                    //status
//                    if (tempDeliver.getHaveRead()) {
//                        dataJson.put("status", "已读");
//                    } else {
//                        dataJson.put("status", "未读");
//                    }
//
//                    dataJsonArray.put(dataJson);
//
//                }
//            }
//            json.put("data", dataJsonArray);
//        }
//        System.out.println(json);
//        SendInfo.render(json.toString(), "text/json", response);
//    }

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
        String deliveryId = request.getParameter("id");
        ResumeSend resumeSend = deliverRepository.findByDeliverId(Long.parseLong(resumeSendId));
        String result;
        if (!company.getCompanyId().equals(resumeSend.getCompanyId())) {
            result = "{\"ok\":\"false\",\"reason\":\"非法删除其他公司收到的信息\"}";
        } else {
            deliverService.updateHaveDeleteByDeliverId(Long.parseLong(deliveryId));
            result = "{\"ok\":\"true\"}";
        }
        SendInfo.render(result, "text/json", response);
    }

    /**
     * 公司请求投递信息对应的简历信息
     */
    @GetMapping(value = "/resume_received/resume")
    public void showCompanyReceivedResumeDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        Object loginUser = request.getSession().getAttribute("loginUser");
        if (loginUser == null || !(loginUser instanceof Company)) {
            response.sendRedirect("/");
            return;
        }
        Company company = (Company)loginUser;
        System.out.println("公司请求投递信息对应得简历信息");
        String resumeSendStr = request.getParameter("resumeSendId");
        if (resumeSendStr == null) {
            System.out.println("resumeSendStr is null");
        }
        Long resumeSendId = Long.parseLong(resumeSendStr);
        ResumeSend resumeSend = deliverRepository.findByDeliverId(resumeSendId);
        System.out.println(resumeSend);
        Long resumeId = resumeSend.getResumeId();
//        查找简历
        Resume resume = resumeRepository.findByResumeId(resumeId);
        SendInfo.render(resume.getResumeContent(), "text/json", response);

    }
}
