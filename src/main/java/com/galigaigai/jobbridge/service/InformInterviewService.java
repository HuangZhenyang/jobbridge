package com.galigaigai.jobbridge.service;

import com.galigaigai.jobbridge.model.InformInterview;
import com.galigaigai.jobbridge.repository.InformInterviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


/**
 * Created by HanrAx on 2018/3/13.
 * 通知面试业务层
 */
@Service
public class InformInterviewService {
    @Autowired
    private InformInterviewRepository informInterviewRepository;

    /**
     * 添加
     * */
    @Transactional
    public void addInformInterview(InformInterview informInterview){
        try {
            informInterviewRepository.save(informInterview);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * 根据informInterviewId删除
     * */
    @Transactional
    @Modifying
    public void deleteInterviewById(Long informInterviewId){
        try {
            InformInterview informInterview =
                    informInterviewRepository.findByInformInterviewId(informInterviewId);
            if(informInterview == null){
                System.out.println("要删除的InformInterview对象不存在");
            }else{
                informInterviewRepository.delete(informInterview);
            }

        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
