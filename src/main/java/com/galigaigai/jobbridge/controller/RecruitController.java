package com.galigaigai.jobbridge.controller;

import com.galigaigai.jobbridge.model.Company;
import com.galigaigai.jobbridge.model.Recruit;
import com.galigaigai.jobbridge.model.RecruitTag;
import com.galigaigai.jobbridge.model.Tag;
import com.galigaigai.jobbridge.repository.CompanyRepository;
import com.galigaigai.jobbridge.repository.RecruitRepository;
import com.galigaigai.jobbridge.repository.RecruitTagRepository;
import com.galigaigai.jobbridge.repository.TagRepository;
import com.galigaigai.jobbridge.util.ParseStringUtil;
import com.galigaigai.jobbridge.util.RecruitUtil;
import com.galigaigai.jobbridge.util.SendInfoUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HanrAx on 2018/3/13 0013.
 */
@Controller
@RequestMapping("/recruit")
public class RecruitController {

    @Autowired
    private RecruitRepository recruitRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private RecruitTagRepository recruitTagRepository;
    @Autowired
    private CompanyRepository companyRepository;

    /**
     * 这里可以无登录访问
     * 请求职海页面
     * */
    @GetMapping(value = "/info")
    public String recruit(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 定义数据结构
        List<Recruit> recruitList = new ArrayList<>();
        List<Company> companyList = new ArrayList<>();
        int recruitNum = (int)recruitRepository.count();
        if(recruitNum == 0){
            model.addAttribute("numberOfPage",0);
            return "recruitSea";
        }
        int limit = 0;
        if(recruitNum < 10){
            limit = recruitNum;
        }else{
            limit = 10;
        }
        recruitList = recruitRepository.findRecruitOrderByTime(0, limit);
        if(recruitList == null || recruitList.isEmpty() || (recruitList.size() == 1 && recruitList.get(0) == null)){
            model.addAttribute("numberOfPage",0);
            return "recruitSea";
        }
//        对这10个（可能少于10个）招聘信息进行处理
        for(Recruit recruit:recruitList){
            Company company = companyRepository.findByCompanyId(recruit.getCompanyId());
            if(company == null){
                System.out.println("内部错误");
                model.addAttribute("numberOfPage",0);
                recruitList.remove(recruit);
                recruitNum--;
                continue;
            }
            companyList.add(company);
        }
        int pageNum = 0;
        if(recruitNum % 10 == 0){
            pageNum = recruitNum/10;
        }
        else{
            pageNum = recruitNum/10 + 1;
        }
        model.addAttribute("numberOfPage",pageNum);
        model.addAttribute("recruitList",recruitList);
        model.addAttribute("companyList",companyList);
        return "recruitSea";
    }

    /**
     * 这里可以无登录访问
     * 职海之showinfo
     * *//*
    @GetMapping(value = "/show_info")
    public void showRecruit(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
//        定义数据结构
        JSONObject json = new JSONObject();
        JSONArray dataJson = new JSONArray();
        int recruitNum = (int)recruitRepository.count();
        if(recruitNum == 0){
            json.put("numberOfPage",0);
            json.put("data",dataJson);
            SendInfoUtil.render(json.toString(),"text/json",response);
            return;
        }
        int limit = 0;
        if(recruitNum < 10){
            limit = recruitNum;
        }else{
            limit = 10;
        }
        List<Recruit> recruitList = recruitRepository.findRecruitOrderByTime(0, limit);
        if(recruitList == null || recruitList.isEmpty() || (recruitList.size() == 1 && recruitList.get(0) == null)){
            json.put("numberOfPage",0);
            json.put("data",dataJson);
            SendInfoUtil.render(json.toString(),"text/json",response);
            return;
        }
//        对这10个（可能少于10个）招聘信息进行处理
        for(Recruit recruit:recruitList){
            Company company = companyRepository.findByCompanyId(recruit.getCompanyId());
            if(company == null){
                System.out.println("内部错误");
                json.put("numberOfPage",0);
                json.put("data",new JSONArray());
                SendInfoUtil.render(json.toString(),"text/json",response);
                return;
            }
            JSONObject recruitJson = new JSONObject();
            recruitJson.put("jobTitle",recruit.getJobName());
            recruitJson.put("jobId",recruit.getRecruitId());
            recruitJson.put("companyName",company.getName());
            recruitJson.put("location",recruit.getLocation());
            recruitJson.put("time",recruit.getDateTime());
            recruitJson.put("companyDesc",company.getCompanyIntroduction());
            recruitJson.put("iconAddress",company.getIconAddress());
            dataJson.put(recruitJson);
        }
        int pageNum = 0;
        if(recruitNum % 10 == 0){
            pageNum = recruitNum/10;
        }
        else{
            pageNum = recruitNum/10 + 1;
        }
        json.put("numberOfPage",pageNum);
        json.put("data",dataJson);
        SendInfoUtil.render(json.toString(),"text/json",response);
    }*/

    /**
     * 这里可以无登录访问
     * 职海之条件筛选页面
     * */
    @PostMapping(value = "/info")
    public void showRecruitByCondition(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        String content = request.getParameter("content");
//        定义收到的数据结构
        JSONObject receiveJson = new JSONObject(content);
        JSONObject receiveOptionJson = null;
        int pageNum = receiveJson.getInt("numberOfPage");
        receiveOptionJson = receiveJson.getJSONObject("optionList");
        String locations = receiveOptionJson.get("cityList").toString();
        String industries = receiveOptionJson.get("industryList").toString();
        String[] locationList = ParseStringUtil.parseString(locations);
        String[] tags = ParseStringUtil.parseString(industries);
        if(pageNum < 10 || pageNum % 10 != 0){
            System.out.println("前台项数错误");
            return;
        }
//        定义发送的数据结构
        JSONObject sendJson = new JSONObject();
        JSONArray sendDataJson = new JSONArray();
        List<Recruit> recruitList = new ArrayList<>();
        int recruitNum = 0;
        if(tags.length == 1 && tags[0].equals("不限")){
            if(locationList.length == 1 && locationList[0].equals("不限")){
//                情况一：城市和行业都不限
                recruitNum = (int)recruitRepository.count();
                if(recruitNum != 0){
                    int limit = recruitNum - pageNum + 10;
                    if(limit >= 10){
                        limit = 10;
                    }
                    recruitList = recruitRepository.findRecruitOrderByTime(pageNum - 10, limit);
                }
            }else{
//                情况二：行业不限，限制城市
                List<Recruit> recruitCityCondition = new ArrayList<>();
                for(int i = 0;i<locationList.length;i++){
                    List<Recruit> tempRecruitList = recruitRepository.findByLocation(locationList[i]);
                    if(tempRecruitList == null || tempRecruitList.isEmpty() ||
                            (tempRecruitList.size() == 1 && tempRecruitList.get(0) == null)){
//                        这里不做处理
                    }else{
                        for(Recruit recruit:tempRecruitList){
                            if(recruit != null){
                                recruitCityCondition.add(recruit);
                            }
                        }
                    }
                }
                recruitNum = recruitCityCondition.size();
                if(recruitNum != 0){
                    int limit = recruitNum - pageNum + 10;
                    if(limit < 10){
                        for(int i = pageNum - 10;i<pageNum - 10 + limit;i++){
                            recruitList.add(recruitCityCondition.get(i));
                        }
                    }else{
                        for(int i = pageNum - 10;i<pageNum;i++){
                            recruitList.add(recruitCityCondition.get(i));
                        }
                    }
                }
            }
        }else{
            if(locationList.length == 1 && locationList[0].equals("不限")){
//                情况三：城市不限，限制行业
                boolean flag = true;
                List<Recruit> recruitCondition = new ArrayList<>();
                for(int i = 0;i< tags.length;i++){
                    Tag tag = tagRepository.findByName(tags[i]);
                    if(tag == null){
                        continue;
                    }
                    List<RecruitTag> recruitTagList = recruitTagRepository.findByTagId(tag.getTagId());
                    if(!(recruitTagList == null || recruitTagList.isEmpty() ||
                            (recruitTagList.size() == 1 && recruitTagList.get(0) == null))){
//                        还没有则加新的，有则在里面找还满足新条件的
                        if(recruitCondition.isEmpty()){
                            for(RecruitTag recruitTag:recruitTagList){
                                if(recruitTag != null){
                                    Recruit recruit = recruitRepository.findByRecruitId(recruitTag.getRecruitId());
                                    if(recruit != null){
                                        recruitCondition.add(recruit);
                                    }
                                }
                            }
//                            找还满足新条件的
                        }else{
                            List<Recruit> temp = new ArrayList<>();
                            for(RecruitTag recruitTag:recruitTagList){
                                if(recruitTag != null){
                                    for(Recruit recruit:recruitCondition){
                                        if(recruit.getRecruitId().equals(recruitTag.getRecruitId())){
                                            temp.add(recruit);
                                        }
                                    }
                                }
                            }
                            if(temp.isEmpty()){
//                                这时没有满足交集条件的招聘信息了
                                flag = false;
                            }
                            recruitCondition.clear();
                            recruitCondition.addAll(temp);
                        }
                    }
                }
                if(flag){
                    recruitNum = recruitCondition.size();
                }else{
                    recruitNum = 0;
                    recruitCondition.clear();
                }
                if(recruitNum != 0){
                    int limit = recruitNum - pageNum + 10;
                    if(limit < 10){
                        for(int i = pageNum - 10;i<pageNum - 10 + limit;i++){
                            recruitList.add(recruitCondition.get(i));
                        }
                    }else{
                        for(int i = pageNum - 10;i<pageNum;i++){
                            recruitList.add(recruitCondition.get(i));
                        }
                    }
                }
            }else {
//                情况四：城市和行业都限制
//                先限制城市
                List<Recruit> recruitCityCondition = new ArrayList<>();
                for(int i = 0;i<locationList.length;i++){
                    List<Recruit> temprecruitList = recruitRepository.findByLocation(locationList[i]);
                    if(!(temprecruitList == null || temprecruitList.isEmpty() ||
                            (temprecruitList.size() == 1 && temprecruitList.get(0) == null))){
                        for(Recruit recruit:temprecruitList){
                            if(recruit != null){
                                recruitCityCondition.add(recruit);
                            }
                        }
                    }
                }
//                再限制行业
                boolean flag = true;
                boolean anyTag = false;
                for(int i = 0;i< tags.length;i++){
                    Tag tag = tagRepository.findByName(tags[i]);
                    if(tag == null){
                        continue;
                    }else{
                        anyTag = true;
                    }
                    List<RecruitTag> recruitTagList = recruitTagRepository.findByTagId(tag.getTagId());
                    if(!(recruitTagList == null || recruitTagList.isEmpty() ||
                            (recruitTagList.size() == 1 && recruitTagList.get(0) == null))){
//                        还没有则加新的，有则在里面找还满足新条件的
                        if(recruitCityCondition.isEmpty() && i != 0){
                            for(RecruitTag recruitTag:recruitTagList){
                                if(recruitTag != null){
                                    Recruit recruit = recruitRepository.findByRecruitId(recruitTag.getRecruitId());
                                    if(recruit != null){
                                        recruitCityCondition.add(recruit);
                                    }
                                }
                            }
//                            找还满足新条件的
                        }else{
                            List<Recruit> temp = new ArrayList<>();
                            for(RecruitTag recruitTag:recruitTagList){
                                if(recruitTag != null){
                                    for(Recruit recruit:recruitCityCondition){
                                        if(recruit.getRecruitId().equals(recruitTag.getRecruitId())){
                                            temp.add(recruit);
                                        }
                                    }
                                }
                            }
                            if(temp.isEmpty()){
//                                这时没有满足交集条件的招聘信息了
                                flag = false;
                            }
                            recruitCityCondition.clear();
                            recruitCityCondition.addAll(temp);
                        }
                    }
                }
                if(flag && anyTag){
                    recruitNum = recruitCityCondition.size();
                }else{
                    recruitNum = 0;
                    recruitCityCondition.clear();
                }
                if(recruitNum != 0){
                    int limit = recruitNum - pageNum + 10;
                    if(limit < 10){
                        for(int i = pageNum - 10;i<pageNum - 10 + limit;i++){
                            recruitList.add(recruitCityCondition.get(i));
                        }
                    }else{
                        for(int i = pageNum - 10;i<pageNum;i++){
                            recruitList.add(recruitCityCondition.get(i));
                        }
                    }
                }
            }
        }
//        处理最终要发送的数据
        int page = 0;
        if(recruitNum != 0 && recruitNum % 10 == 0){
            page = recruitNum / 10;
        }else if(recruitNum % 10 != 0){
            page = recruitNum / 10 + 1;
        }
        sendJson.put("numberOfPage",page);
        if(!recruitList.isEmpty()){
            List<Recruit> resultList = RecruitUtil.orderByTime(recruitList);
            for(Recruit recruit:resultList){
                Company company = companyRepository.findByCompanyId(recruit.getCompanyId());
                if(company == null){
                    System.out.println("内部错误:找不到公司");
                    return;
                }
                JSONObject sendRecruitJson = new JSONObject();
                sendRecruitJson.put("jobTitle",recruit.getJobName());
                sendRecruitJson.put("jobId",recruit.getRecruitId());
                sendRecruitJson.put("location",recruit.getLocation());
                sendRecruitJson.put("time",recruit.getDateTime());
                sendRecruitJson.put("companyDesc",company.getCompanyIntroduction());
                sendRecruitJson.put("iconAddress",company.getIconAddress());
                sendRecruitJson.put("companyName",company.getName());
                sendDataJson.put(sendRecruitJson);
            }
        }
        sendJson.put("data",sendDataJson);
        SendInfoUtil.render(sendJson.toString(),"text/json",response);
    }
}
