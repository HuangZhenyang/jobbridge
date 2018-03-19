package com.galigaigai.jobbridge.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by SYunk on 2018/3/18.
 * 行业类
 */

@Entity
public class Industry {
    @Id
    @GeneratedValue
    private Integer industryId;

    private String name;

    public Industry(){

    }

    public Industry(Integer industryId, String name) {
        this.industryId = industryId;
        this.name = name;
    }

    public Integer getIndustryId() {
        return industryId;
    }
    public String getName() {
        return name;
    }

    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }
    public void setName(String name) {
        this.name = name;
    }
}
