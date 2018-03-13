package com.galigaigai.jobbridge.repository;

import com.galigaigai.jobbridge.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SYunk on 2018/3/13.
 * 学生持久化层
 */
public interface StudentRepository extends JpaRepository<Student,Long> {
    Student findByUserName(String userName); //根据用户名查找学生
    Student findByStudentId(Long studentId);  //通过学生ID查找学生
    Student findByMailbox(String mailbox); //根据邮箱查找学生

}
