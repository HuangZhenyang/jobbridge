package com.galigaigai.jobbridge.repository;

import com.galigaigai.jobbridge.model.RecruitTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by SYunk on 2018/3/13.
 * 招聘信息大标签持久化层
 */
public interface RecruitTagRepository extends JpaRepository<RecruitTag,Long> {
    List<RecruitTag> findByRecruitId(Long recruitId);
    List<RecruitTag> findByTagId(Integer tagId);
}
