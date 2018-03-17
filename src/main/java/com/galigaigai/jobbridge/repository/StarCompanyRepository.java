package com.galigaigai.jobbridge.repository;

import com.galigaigai.jobbridge.model.StarCompany;
import com.galigaigai.jobbridge.model.StarCompanyMultiKeysClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by HuangZhenyang on 2018/3/13.
 * 收藏公司持久化层
 */
public interface StarCompanyRepository extends JpaRepository<StarCompany, StarCompanyMultiKeysClass> {

    List<StarCompany> findByStudentId(Long studentId);          //学生查询自己收藏的所有公司
}
