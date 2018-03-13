package com.galigaigai.jobbridge.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by SYunk on 2018/3/13.
 * 学生详细信息类
 */

@Entity
public class StudentDetail {
    @Id
    private Long studentId;

    private String studentMailbox;
    private String phoneNum;
    private String universityName;
    private String major;
    private String grade;
    private String intentionCity;
    private String intentionIndustry;
    private String intentionFunction;
    private Boolean authentication;

    public StudentDetail(){

    }

    public StudentDetail(Long studentId, String studentMailbox, String phoneNum, String universityName, String major, String grade,
                         String intentionCity, String intentionIndustry, String intentionFunction, Boolean authentication) {
        this.studentId = studentId;
        this.studentMailbox = studentMailbox;
        this.phoneNum = phoneNum;
        this.universityName = universityName;
        this.major = major;
        this.grade = grade;
        this.intentionCity = intentionCity;
        this.intentionIndustry = intentionIndustry;
        this.intentionFunction = intentionFunction;
        this.authentication = authentication;
    }

    public Long getStudentId() {
        return studentId;
    }
    public String getStudentMailbox() {
        return studentMailbox;
    }
    public String getPhoneNum() {
        return phoneNum;
    }
    public String getUniversityName() {
        return universityName;
    }
    public String getMajor() {
        return major;
    }
    public String getGrade() {
        return grade;
    }
    public String getIntentionCity() {
        return intentionCity;
    }
    public String getIntentionIndustry() {
        return intentionIndustry;
    }
    public String getIntentionFunction() {
        return intentionFunction;
    }
    public Boolean getAuthentication() {
        return authentication;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    public void setStudentMailbox(String studentMailbox) {
        this.studentMailbox = studentMailbox;
    }
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }
    public void setMajor(String major) {
        this.major = major;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public void setIntentionCity(String intentionCity) {
        this.intentionCity = intentionCity;
    }
    public void setIntentionIndustry(String intentionIndustry) {
        this.intentionIndustry = intentionIndustry;
    }
    public void setIntentionFunction(String intentionFunction) {
        this.intentionFunction = intentionFunction;
    }
    public void setAuthentication(Boolean authentication) {
        this.authentication = authentication;
    }
}
