package com.galigaigai.jobbridge.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by SYunk on 2018/3/20.
 * 城市类
 */

@Entity
public class City {
    @Id
    @GeneratedValue
    private Integer cityId;

    private String name;

    public City() {

    }

    public City(String name) {
        this.name = name;
    }

    public Integer getCityId() {
        return cityId;
    }
    public String getName() {
        return name;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
    public void setName(String name) {
        this.name = name;
    }
}
