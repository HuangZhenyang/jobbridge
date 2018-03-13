package com.galigaigai.jobbridge.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by SYunk on 2018/3/13.
 * 学生类
 */

@Entity
public class Student {
    @Id
    @GeneratedValue
    private Long studentId;

    private String userName;
    private String mailbox;
    private String password;
    private String identity;

    public Student(){

    }

    public Student(String userName, String mailbox, String password, String identity) {
        this.userName = userName;
        this.mailbox = mailbox;
        this.password = password;
        this.identity = identity;
    }

    public Long getStudentId() {
        return studentId;
    }
    public String getUserName() {
        return userName;
    }
    public String getMailbox() {
        return mailbox;
    }
    public String getPassword() {
        return password;
    }
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }
}
