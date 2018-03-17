package com.galigaigai.jobbridge.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * Created by GeniusHe on 2018/3/13.
 * 收藏公司类
 */

@Entity
@Table(name = "star_company")
@IdClass(StarCompanyMultiKeysClass.class)
public class StarCompany {
    private Long companyId;  //被收藏的公司ID
    private Long studentId;     //发起收藏请求的学生ID

    public StarCompany(){

    }

    public StarCompany(Long companyId, Long studentId) {
        this.companyId = companyId;
        this.studentId = studentId;
    }

    @Id
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Id
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
