package com.galigaigai.jobbridge.repository;

import com.galigaigai.jobbridge.model.StudentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SYunk on 2018/3/13.
 * 学生详细信息持久化层
 */
public interface StudentDetailRepository extends JpaRepository<StudentDetail,Long> {

    StudentDetail findByStudentId(Long studentId);
}
