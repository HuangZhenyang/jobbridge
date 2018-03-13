package com.galigaigai.jobbridge.service;

import com.galigaigai.jobbridge.model.StarCompany;
import com.galigaigai.jobbridge.model.StarCompanyMultiKeysClass;
import com.galigaigai.jobbridge.repository.StarCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Map;

/**
 * Created by HanrAx on 2018/3/13.
 * 收藏公司业务层
 */
@Service
public class StarCompanyService {

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private StarCompanyRepository starCompanyRepository;

    /**
     * 添加
     * */
    @Transactional
    public void addStarCompany(StarCompany starCompany){
        try {
            starCompanyRepository.save(starCompany);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * 删除
     * <"student_id",12>
     * <"company_id",11>
     * */
    @Transactional
    @Modifying
    public void deleteById(Map<String, Object> map){
        Long companyId = (Long)map.get("companyId");
        Long studentId = (Long)map.get("studentId");

        StarCompanyMultiKeysClass starCompanyMultiKeysClass =
                new StarCompanyMultiKeysClass(companyId, studentId);
        StarCompany starCompany =
                entityManager.find(StarCompany.class, starCompanyMultiKeysClass);
        if(starCompany == null){
            System.out.println("没有找到要删除的starCompany对象");
        }else{
            starCompanyRepository.delete(starCompany);
        }
    }
}
