package com.galigaigai.jobbridge.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * Created by SYunk on 2018/3/13.
 * 招聘信息大标签类
 */

@Entity
@Table(name = "recruit_tag")
@IdClass(RecruitTagMultiKeysClass.class)
public class RecruitTag {
    private Long recruitId;
    private Integer tagId;

    public RecruitTag(){

    }

    public RecruitTag(Long recruitId, Integer tagId) {
        this.recruitId = recruitId;
        this.tagId = tagId;
    }

    @Id
    public Long getRecruitId() {
        return recruitId;
    }

    @Id
    public Integer getTagId() {
        return tagId;
    }

    public void setRecruitId(Long recruitId) {
        this.recruitId = recruitId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
}
