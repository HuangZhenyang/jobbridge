package com.galigaigai.jobbridge.repository;

import com.galigaigai.jobbridge.model.Resume;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by SYunk on 2018/3/13.
 * 简历持久化层
 */
public interface ResumeRepository extends JpaRepository<Resume,Long> {
    Resume findByStudentId(Long studentId);
    Resume findByResumeId(Long resumeId); //通过resumeID找到resume实例
}
