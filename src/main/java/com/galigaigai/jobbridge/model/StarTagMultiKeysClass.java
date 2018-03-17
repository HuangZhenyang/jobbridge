package com.galigaigai.jobbridge.model;

import java.io.Serializable;

/**
 * Created by huangzhenyang on 2018/3/13.
 * StarTag的复合主键类
 */


public class StarTagMultiKeysClass implements Serializable{
    private Long studentId;     //发起收藏请求的学生ID
    private Integer tagId;      //被收藏的标签ID

    // constructor
    public StarTagMultiKeysClass(){

    }

    public StarTagMultiKeysClass(Long studentId, Integer tagId) {
        this.studentId = studentId;
        this.tagId = tagId;
    }

    // getter and setter
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    // 重写hashcode 和 equals方法
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((studentId == null) ? 0 : studentId.hashCode());
        result = PRIME * result + ((tagId == null) ? 0 : tagId.hashCode());
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

        final StarTagMultiKeysClass other = (StarTagMultiKeysClass)obj;

        if(studentId == null){
            if(other.studentId != null){
                return false;
            }
        }else if(!studentId.equals(other.studentId)){
            return false;
        }

        if(tagId == null){
            if(other.tagId != null){
                return false;
            }
        }else if(!tagId.equals(other.tagId)){
            return false;
        }

        return true;
    }
}
