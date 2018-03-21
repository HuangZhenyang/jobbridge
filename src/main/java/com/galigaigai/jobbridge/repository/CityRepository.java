package com.galigaigai.jobbridge.repository;

import com.galigaigai.jobbridge.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SYunk on 2018/3/20.
 * 城市类持久化层
 */

public interface CityRepository extends JpaRepository<City,Long> {

    City findByCityId(Integer cityId); // 通过城市id查找城市
    City findByName(String name); // 通过城市名查找城市
}
