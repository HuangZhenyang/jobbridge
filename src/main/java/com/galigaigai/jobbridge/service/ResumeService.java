package com.galigaigai.jobbridge.service;

import com.galigaigai.jobbridge.model.Resume;
import com.galigaigai.jobbridge.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by SYunk on 2018/3/13.
 * 简历业务层
 */

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    /**
     * 添加简历
     * */
    @Transactional
    public void addResume(Resume resume){
        try {
            resumeRepository.save(resume);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * 修改简历
     * */
    @Transactional
    @Modifying
    public void updateResume(Resume resume){
        Resume oldResume = resumeRepository.findByResumeId(resume.getResumeId());
        if(oldResume == null){
            System.out.println("要修改的简历不存在");
        }else{
            resumeRepository.save(resume);
        }
    }
}
