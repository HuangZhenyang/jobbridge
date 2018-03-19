package com.galigaigai.jobbridge.service;

import com.galigaigai.jobbridge.model.Company;
import com.galigaigai.jobbridge.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
     * 查找已审核的公司与没有审核的公司
     * @param AuditOrNot true为选择已审核过的所有公司，false为选择为审核的公司
     * @return 根据AuditOrNot的值，返回不同的company List
     */
    @Transactional
    public List<Company> findCompanyAuditOrNot(Boolean AuditOrNot){
        List<Company> companyList = companyRepository.findAll();
        List<Company> resultCompanyList  = new ArrayList<>();
        for (Company company : companyList) {
            if (company.getAuditing() == AuditOrNot){
                resultCompanyList.add(company);
            }
        }
        return resultCompanyList;
    }
}
