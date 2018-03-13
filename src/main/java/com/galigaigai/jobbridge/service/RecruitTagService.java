package com.galigaigai.jobbridge.service;

import com.galigaigai.jobbridge.model.RecruitTag;
import com.galigaigai.jobbridge.repository.RecruitTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by SYunk on 2018/3/13.
 * 招聘信息大标签业务层
 */
@Service
public class RecruitTagService {

    @Autowired
    private RecruitTagRepository recruitTagRepository;

    /**
     * 添加
     * */
    @Transactional
    public void addRecruitTag(RecruitTag recruitTag){
        try {
            recruitTagRepository.save(recruitTag);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * 删除该招聘信息对应的所有标签
     */
    @Transactional
    @Modifying
    public void deleteRecruitTagByRecruitId(Long recruitId){
        List<RecruitTag> recruitTagList =
                recruitTagRepository.findByRecruitId(recruitId);
        for(RecruitTag recruitTag : recruitTagList){
            recruitTagRepository.delete(recruitTag);
        }
    }
}
