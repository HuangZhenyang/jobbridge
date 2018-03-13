package com.galigaigai.jobbridge.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Created by GeniusHe on 2018/3/13.
 * 简历投递类
 */
@Entity
public class ResumeSend {
    @Id
    @GeneratedValue
    private Long resumeSendId;

    private Long resumeId;      //学生对应简历Id
    private Long companyId;  //被投简历的公司Id
    private Long recruitId;         //招聘信息号
    private Timestamp dateTime; //投递日期时间
    private Boolean haveRead;
    private Boolean haveDelete;

    public ResumeSend(){

    }

    public ResumeSend(Long resumeSendId, Long resumeId, Long companyId, Long recruitId, Timestamp dateTime, Boolean haveRead, Boolean haveDelete) {
        this.resumeSendId = resumeSendId;
        this.resumeId = resumeId;
        this.companyId = companyId;
        this.recruitId = recruitId;
        this.dateTime = dateTime;
        this.haveRead = haveRead;
        this.haveDelete = haveDelete;
    }


    public Long getResumeSendId() {
        return resumeSendId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public Long getRecruitId() {
        return recruitId;
    }

    public Long getResumeId() {
        return resumeId;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public Boolean getHaveRead() {
        return haveRead;
    }

    public Boolean getHaveDelete() {
        return haveDelete;
    }


    public void setResumeSendId(Long resumeSendId) {
        this.resumeSendId = resumeSendId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public void setRecruitId(Long recruitId) {
        this.recruitId = recruitId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public void setHaveRead(Boolean haveRead) {
        this.haveRead = haveRead;
    }

    public void setHaveDelete(Boolean haveDelete) {
        this.haveDelete = haveDelete;
    }
}
