package com.galigaigai.jobbridge.service;

import com.galigaigai.jobbridge.model.Company;
import com.galigaigai.jobbridge.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


/**
 * Created by SYunk on 2018/3/13.
 * 公司业务层
 */
@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    /**
     * 添加公司
     * */
    @Transactional
    public void addCompany(Company company){
        try {
            companyRepository.save(company);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * 查询得到所有的公司
     * */
    public List<Company> findAllCompany(){
        return companyRepository.findAll();
    }
}
