package com.galigaigai.jobbridge.repository;

import com.galigaigai.jobbridge.model.StarTag;
import com.galigaigai.jobbridge.model.StarTagMultiKeysClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by HanrAx on 2018/3/13.
 * 学生收藏大标签持久化层
 */
public interface StarTagRepository extends JpaRepository<StarTag,StarTagMultiKeysClass>{
    List<StarTag> findByStudentId(Long studentId);     //学生查询所有自己收藏过的便签
}
