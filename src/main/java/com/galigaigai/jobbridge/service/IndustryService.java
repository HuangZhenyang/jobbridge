package com.galigaigai.jobbridge.service;

import com.galigaigai.jobbridge.model.Industry;
import com.galigaigai.jobbridge.repository.IndustryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by SYunk on 2018/3/18.
 * 行业类业务层
 */

@Service
public class IndustryService {

    @Autowired
    private IndustryRepository industryRepository;

    @Transactional
    public void addIndustry(Industry industry){
        try {
            industryRepository.save(industry);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
