package com.galigaigai.jobbridge.model;

import java.io.Serializable;

/**
 * Created by huangzhenyang on 2018/3/13.
 * RecruitInfoTag的复合主键类
 */

public class RecruitTagMultiKeysClass implements Serializable{
    private Long recruitInfoId;
    private Integer tagId;

    // constructor
    public RecruitTagMultiKeysClass(){

    }

    public RecruitTagMultiKeysClass(Long recruitInfoId, Integer tagId) {
        this.recruitInfoId = recruitInfoId;
        this.tagId = tagId;
    }

    // getter and setter
    public Long getRecruitInfoId() {
        return recruitInfoId;
    }

    public void setRecruitInfoId(Long recruitInfoId) {
        this.recruitInfoId = recruitInfoId;
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
        result = PRIME * result + ((recruitInfoId == null) ? 0 : recruitInfoId.hashCode());
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

        final RecruitTagMultiKeysClass other = (RecruitTagMultiKeysClass)obj;

        if(recruitInfoId == null){
            if(other.recruitInfoId != null){
                return false;
            }
        }else if(!recruitInfoId.equals(other.recruitInfoId)){
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
