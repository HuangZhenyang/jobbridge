package com.galigaigai.jobbridge.repository;

import com.galigaigai.jobbridge.model.ResumeSend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by HanrAx on 2018/3/13.
 * 简历投递持久化层
 */
public interface ResumeSendRepository extends JpaRepository<ResumeSend, Long>{
    /**
     * 通过简历ID、公司ID、查找所有的投递信息
     * @param
     * @return
     */
    List<ResumeSend> findByResumeIdAndCompanyId(Long resumeId, Long companyId);

    //公司查找所有收到的投递信息，并可以根据其中的简历查看该学生的简历
    List<ResumeSend> findByCompanyId(Long companyId);

    //根据投递号查询投递信息
    ResumeSend findByResumeSendId(Long resumeSendId);

    //根据简历号查询投递信息
    List<ResumeSend> findByResumeId(Long resumeId);

    //根据学生id和招聘信息id查询投递信息
    ResumeSend findByResumeIdAndRecruitId(Long resumeId, Long RecruitId);

}
