package com.galigaigai.jobbridge.service;

import com.galigaigai.jobbridge.model.Review;
import com.galigaigai.jobbridge.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


/**
 * Created by HanrAx on 2018/3/13.
 * 公司职位评论业务层
 */

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    /**
     * 新增评论
     * */
    @Transactional
    public void addReview(Review review){
        try {
            reviewRepository.save(review);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * 删除评论
     * */
    @Transactional
    @Modifying
    public void deleteReview(Long studentId, Long companyId){
        Review review = reviewRepository.findByStudentIdAndCompanyId(studentId, companyId);
        if(review == null){
            System.out.println("没找到要删除的评论");
        }else{
            reviewRepository.delete(review);
        }
    }
}
