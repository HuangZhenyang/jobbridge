package com.galigaigai.jobbridge.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * Created by GeniusHe on 2018/3/13.
 * 学生收藏的大标签类
 */

@Entity
@Table(name = "star_tag")
@IdClass(StarTagMultiKeysClass.class)
public class StarTag {
    private Long studentId;     //发起收藏请求的学生ID
    private Integer tagId;      //被收藏的标签ID

    public StarTag(){

    }

    public StarTag(Long studentId, Integer tagId) {
        this.studentId = studentId;
        this.tagId = tagId;
    }

    @Id
    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    @Id
    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }
}
