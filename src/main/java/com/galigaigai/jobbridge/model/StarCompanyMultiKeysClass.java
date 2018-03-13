package com.galigaigai.jobbridge.model;

import java.io.Serializable;

/**
 * Created by huangzhenyang on 2018/3/13.
 * CollectEnterprise的复合主键类
 */

public class StarCompanyMultiKeysClass implements Serializable {
    private Long enterpriseId;  //被收藏的公司ID
    private Long studentId;     //发起收藏请求的学生ID

    // constructor
    public StarCompanyMultiKeysClass(){

    }

    public StarCompanyMultiKeysClass(Long enterpriseId, Long studentId){
        this.enterpriseId = enterpriseId;
        this.studentId = studentId;
    }

    // getter and setter
    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    // 重写hashcode 和 equals方法
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((enterpriseId == null) ? 0 : enterpriseId.hashCode());
        result = PRIME * result + ((studentId == null) ? 0 : studentId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(getClass() != obj.getClass()){
            return false;
        }

        final StarCompanyMultiKeysClass other = (StarCompanyMultiKeysClass)obj;
        if(enterpriseId == null){
            if(other.enterpriseId != null){
                return false;
            }
        }else if(!enterpriseId.equals(other.enterpriseId)){
            return false;
        }

        if(studentId == null){
            if(other.studentId != null){
                return false;
            }
        }else if(!studentId.equals(other.studentId)){
            return false;
        }

        return true;
    }
}
