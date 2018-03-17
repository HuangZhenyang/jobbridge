package com.galigaigai.jobbridge.repository;

import com.galigaigai.jobbridge.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by HanrAx on 2018/3/13.
 * 公司职位评论持久化层
 */
public interface ReviewRepository extends JpaRepository<Review,Long> {
    List<Review> findByStudentId(Long studentId);           //学生可以查询自己发过的评论
    List<Review> findByCompanyId(Long companyId);     //公司可以查询收到的评论
    Review findByStudentIdAndCompanyId(Long studentId, Long companyId); // 通过学生ID和公司ID查询唯一一条评论
}
