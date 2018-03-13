package com.galigaigai.jobbridge.service;

import com.galigaigai.jobbridge.model.Student;
import com.galigaigai.jobbridge.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


/**
 * Created by SYunk on 2018/3/13.
 * 学生业务层
 */

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    /**
     * 新增注册的学生账号
     * */
    @Transactional
    public void addStudent(Student student){
        try {
            studentRepository.save(student);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
