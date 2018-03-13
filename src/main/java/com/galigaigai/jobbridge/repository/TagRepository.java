package com.galigaigai.jobbridge.repository;

import com.galigaigai.jobbridge.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2018/3/13.
 * 标签持久化层
 */
public interface TagRepository extends JpaRepository<Tag,Integer> {
    Tag findByTagId(Integer tagId);   //通过tagId找到标签实体
    Tag findByName(String name);
}
