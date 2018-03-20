package com.galigaigai.jobbridge.repository;


import com.galigaigai.jobbridge.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by SYunk on 2018/3/13.
 * 公司持久化层
 */
public interface CompanyRepository extends JpaRepository<Company,Long> {

    Company findByCompanyId(Long companyId); //根据公司号查询公司

    Company findByUserName(String userName); //根据用户名查找公司

    Company findByMailbox(String mailbox); //根据邮箱查找公司


    List<Company> findAll(); // 得到所有的公司
}
