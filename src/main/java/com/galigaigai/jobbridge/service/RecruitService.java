package com.galigaigai.jobbridge.service;

import com.galigaigai.jobbridge.model.Recruit;
import com.galigaigai.jobbridge.repository.RecruitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;


/**
 * Created by SYunk on 2018/3/13.
 * 招聘信息业务层
 */
@Service
public class RecruitService {

    @Autowired
    private RecruitRepository recruitRepository;


    /**
     * 更新haveDelete
     * */
    @Transactional
    @Modifying
    public void updateHaveDeleteById(Long recruitId){
        Recruit recruit = recruitRepository.findByRecruitId(recruitId);
        if(recruit == null){
            System.out.println("没找到需要更新的Recruit");
        }else{
            recruit.setHaveDelete(true);
            recruitRepository.save(recruit);
        }
    }


    /**
     * 添加
     * */
    @Transactional
    public void addRecruit(Recruit recruit){
        try {
            recruitRepository.save(recruit);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }


    /**
     * 更新
     * */
    @Transactional
    @Modifying
    public void updateRecruitById(Map<String,Object> map){

        Long recruitId = (Long)map.get("recruitId");
        String jobName = (String)map.get("jobName");
        String jobDescribe = (String)map.get("jobDescribe");
        String jobRequire = (String)map.get("jobRequire");
        String location = (String)map.get("location");
        Integer lowSalary = (Integer)map.get("lowSalary");
        Integer highSalary = (Integer)map.get("highSalary");
        String deadline = (String) map.get("deadline");

        Recruit recruit = recruitRepository.findByRecruitId(recruitId);
        if(recruit == null){
            System.out.println("需要更新的Recruit不存在");
        }else{
            recruit.setJobName(jobName);
            recruit.setJobDescribe(jobDescribe);
            recruit.setJobRequire(jobRequire);
            recruit.setLocation(location);
            recruit.setLowSalary(lowSalary);
            recruit.setHighSalary(highSalary);
            recruit.setDeadline(deadline);

            recruitRepository.save(recruit);
        }

    }

    /**
     * 根据id删除
     * */
    @Transactional
    @Modifying
    public void deleteRecruitById(Long recruitId){
        Recruit recruit = recruitRepository.findByRecruitId(recruitId);
        if(recruit == null){
            System.out.println("要删除的Recruit不存在");
        }else{
            recruitRepository.delete(recruit);
        }
    }
}
