package com.galigaigai.jobbridge.service;

import com.galigaigai.jobbridge.model.ResumeSend;
import com.galigaigai.jobbridge.repository.ResumeSendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by HanrAx on 2018/3/13.
 * 简历投递业务层
 */
@Service
public class ResumeSendService {

    @Autowired
    private ResumeSendRepository resumeSendRepository;

    /**
     * 更新是否删除标记
     * */
    @Transactional
    @Modifying
    public void updateHaveDeleteByResumeSendId(Long resumeSendId){
        ResumeSend resumeSend = resumeSendRepository.findByResumeSendId(resumeSendId);
        if(resumeSend == null){
            System.out.println("该resumeSend不存在");
        }else{
            resumeSend.setHaveDelete(true);
            resumeSendRepository.save(resumeSend);
        }
    }
    /**
     * 更新是否阅读标记
     * */
    @Transactional
    @Modifying
    public void updateHaveReadByResumeSendId(Long resumeSendId){
        ResumeSend resumeSend = resumeSendRepository.findByResumeSendId(resumeSendId);
        if(resumeSend == null){
            System.out.println("该resumeSend不存在");
        }else{
            resumeSend.setHaveRead(true);
            resumeSendRepository.save(resumeSend);
        }
    }

    /**
     * 添加
     * */
    @Transactional
    public void addResumeSend(ResumeSend resumeSend){
        try {
            resumeSendRepository.save(resumeSend);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
