package com.galigaigai.jobbridge.repository;

import com.galigaigai.jobbridge.model.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * Created by SYunk on 2018/3/13.
 * 招聘信息持久化层
 */
public interface RecruitRepository extends JpaRepository<Recruit,Long> {
    @Query(value = "select * from recruit where recruit_id = :recruitId and have_delete = false", nativeQuery = true)
    Recruit findByRecruitId(@Param("recruitId") Long recruitId);

    List<Recruit> findByCompanyId(Long companyId);

    @Query(value = "select * from recruit where company_id = :companyId and date_time >= all( select date_time from recruit where company_id = :companyId )", nativeQuery = true)
    Recruit findLastRecruitByCompanyId(@Param("companyId") Long companyId);

    @Query(value = "select * from recruit where location = :location and have_delete = false", nativeQuery = true)
    List<Recruit> findByLocation(@Param("location") String location);

    long count();

    @Query(value = "select * from recruit where have_delete = false order by date_time desc limit :offset, :limit", nativeQuery = true)
    List<Recruit> findRecruitOrderByTime(@Param("offset") Integer offset, @Param("limit") Integer limit); // Map<String,Object> map

}
