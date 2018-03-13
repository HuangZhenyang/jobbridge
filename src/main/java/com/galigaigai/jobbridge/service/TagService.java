package com.galigaigai.jobbridge.service;

import com.galigaigai.jobbridge.model.Tag;
import com.galigaigai.jobbridge.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by Administrator on 2018/3/13.
 * 标签业务层
 */

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    public void addTag(Tag tag){
        try {
            tagRepository.save(tag);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
