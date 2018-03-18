package com.galigaigai.jobbridge.repository;

import com.galigaigai.jobbridge.model.Industry;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by SYunk on 2018/3/18.
 * 行业类持久化层
 */
public interface IndustryRepository extends JpaRepository<Industry,Integer> {
    Industry findByIndustryId(Integer industryId);
    Industry findByName(String name);
}
