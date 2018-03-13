package com.galigaigai.jobbridge.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by SYunk on 2018/3/13.
 * 简历类
 */

@Entity
public class Resume {
    @Id
    @GeneratedValue
    private Long resumeId;

    private Long studentId;
    private String resumeContent;

    public Resume() {

    }

    public Resume(Long resumeId, Long studentId, String resumeContent) {
        this.resumeId = resumeId;
        this.studentId = studentId;
        this.resumeContent = resumeContent;
    }

    public Long getResumeId() {
        return resumeId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getResumeContent() {
        return resumeContent;
    }

    public void setResumeContent(String resumeContent) {
        this.resumeContent = resumeContent;
    }
}
