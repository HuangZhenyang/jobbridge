package com.galigaigai.jobbridge.model;

import java.io.Serializable;

/**
 * Created by huangzhenyang on 2018/3/13.
 * RecruitTag的复合主键类
 */

public class RecruitTagMultiKeysClass implements Serializable{
    private Long recruitId;
    private Integer tagId;

    // constructor
    public RecruitTagMultiKeysClass(){

    }

    public RecruitTagMultiKeysClass(Long recruitId, Integer tagId) {
        this.recruitId = recruitId;
        this.tagId = tagId;
    }

    public Long getRecruitId() {
        return recruitId;
    }

    public void setRecruitId(Long recruitId) {
        this.recruitId = recruitId;
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
        result = PRIME * result + ((recruitId == null) ? 0 : recruitId.hashCode());
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

        if(recruitId == null){
            if(other.recruitId != null){
                return false;
            }
        }else if(!recruitId.equals(other.recruitId)){
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
