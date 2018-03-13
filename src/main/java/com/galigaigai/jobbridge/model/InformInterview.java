package com.galigaigai.jobbridge.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Created by GeniusHan on 2018/3/13.
 *  面试通知类
 */
@Entity
public class InformInterview {
    @Id
    @GeneratedValue
    private Long informInterviewId;   //通知ID

    private Long companyId;  //面试发出公司ID
    private Long studentId;     //面试学生ID
    private String content;     //通知内容
    private Timestamp dateTime; //通知时间

    public InformInterview(){

    }

    public InformInterview(Long informInterviewId, Long companyId, Long studentId, String content, Timestamp dateTime) {
        this.informInterviewId = informInterviewId;
        this.companyId = companyId;
        this.studentId = studentId;
        this.content = content;
        this.dateTime = dateTime;
    }

    public Long getInformInterviewId() {
        return informInterviewId;
    }

    public void setInformInterviewId(Long informInterviewId) {
        this.informInterviewId = informInterviewId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }
}
