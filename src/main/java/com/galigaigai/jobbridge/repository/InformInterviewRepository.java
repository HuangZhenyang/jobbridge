package com.galigaigai.jobbridge.repository;

import com.galigaigai.jobbridge.model.InformInterview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by HanrAx on 2018/3/13.
 * 通知面试持久化层
 */
public interface InformInterviewRepository extends JpaRepository<InformInterview,Long> {

    InformInterview findByInformInterviewId(Long interviewId);                    //通过通知的ID来查找该通知
    List<InformInterview> findByCompanyId(Long companyId);  //通过公司ID来查找所有公司发过的面试通知
    List<InformInterview> findByStudentId(Long studentId);        //通过学生ID来查找所有学生收到的面试通知

}
