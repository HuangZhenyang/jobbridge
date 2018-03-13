package com.galigaigai.jobbridge.service;

import com.galigaigai.jobbridge.model.StarTag;
import com.galigaigai.jobbridge.model.StarTagMultiKeysClass;
import com.galigaigai.jobbridge.repository.StarTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Created by HanrAx on 2018/3/13.
 * 收藏大标签业务层
 */
@Service
public class StarTagService {
    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private StarTagRepository starTagRepository;

    /**
     * 添加
     * */
    @Transactional
    public void addStarTag(StarTag starTag){
        try {
            starTagRepository.save(starTag);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * 删除该学生收藏的所有标签
     * */
    @Transactional
    @Modifying
    public void deleteStarTagByStudentId(Long studentId){
        List<StarTag> starTagList = starTagRepository.findByStudentId(studentId);
        for (StarTag starTag : starTagList){
            starTagRepository.delete(starTag);
        }
    }

    /**
     * 删除学生收藏的某个标签
     * */
    @Transactional
    @Modifying
    public void deleteStarTagById(Map<String, Object> map){
        Long studentId = (Long)map.get("studentId");
        Integer tagId = (Integer)map.get("tagId");

        StarTagMultiKeysClass starTagMultiKeysClass =
                new StarTagMultiKeysClass(studentId, tagId);

        StarTag starTag = entityManager.find(StarTag.class, starTagMultiKeysClass);
        if(starTag == null){
            System.out.println("没有找到要删除的StarCompany对象");
        }else{
            starTagRepository.delete(starTag);
        }
    }
}
