package com.galigaigai.jobbridge.service;

import com.galigaigai.jobbridge.model.StudentDetail;
import com.galigaigai.jobbridge.repository.StudentDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by SYunk on 2018/3/13.
 * 学生详细信息业务层
 */

@Service
public class StudentDetailService {

    @Autowired
    private StudentDetailRepository studentDetailRepository;

    /**
     * 添加学生详细信息
     * */
    @Transactional
    public void addStudentDetail(StudentDetail studentDetail){
        try {
            studentDetailRepository.save(studentDetail);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * 删除学生详细信息
     * */
    @Transactional
    @Modifying
    public void deleteByStudentId(Long studentId){
        StudentDetail studentDetail = studentDetailRepository.findByStudentId(studentId);
        if(studentDetail != null){
            studentDetailRepository.delete(studentDetail);
        }
    }

    /**
     * 通过学生id，修改学生验证位
     * */
    @Transactional
    @Modifying
    public void updateStudentDetailAuthenticationByStudentId(Long studentId){
        StudentDetail studentDetail = studentDetailRepository.findByStudentId(studentId);
        if(studentDetail == null){
            System.out.println("该学生不存在");
        }else{
            studentDetail.setAuthentication(true);
            studentDetailRepository.save(studentDetail);
        }
    }
}
