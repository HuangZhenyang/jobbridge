package com.galigaigai.jobbridge.service;

import com.galigaigai.jobbridge.model.StudentDetail;
import com.galigaigai.jobbridge.repository.StudentDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Created by SYunk on 2018/3/13.
 * 学生详细信息业务层
 */

@Service
public class StudentDetailService {

    @Autowired
    private StudentDetailRepository studentDetailRepository;

    /**
     * 添加学生详细信息
     * */
    @Transactional
    public void addStudentDetail(StudentDetail studentDetail){
        try {
            studentDetailRepository.save(studentDetail);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * 删除学生详细信息
     * */
    @Transactional
    @Modifying
    public void deleteByStudentId(Long studentId){
        StudentDetail studentDetail = studentDetailRepository.findByStudentId(studentId);
        if(studentDetail != null){
            studentDetailRepository.delete(studentDetail);
        }
    }

    /**
     * 通过学生id，修改学生验证位
     * */
    @Transactional
    @Modifying
    public void updateStudentDetailAuthenticationByStudentId(Long studentId){
        StudentDetail studentDetail = studentDetailRepository.findByStudentId(studentId);
        if(studentDetail == null){
            System.out.println("该学生不存在");
        }else{
            studentDetail.setAuthentication(true);
            studentDetailRepository.save(studentDetail);
        }
    }

    /**
     * 通过某学生的详细信息去查找可能与他有关的人
     * @param studentDetail
     * @return
     */
    @Transactional
    public List<StudentDetail> findStudentDetailByStudentDetail(StudentDetail studentDetail){
        List<StudentDetail> studentDetailList = studentDetailRepository.findAll();
        List<StudentDetail> tempStudentDetailList = new ArrayList<>();
        String universityName = studentDetail.getUniversityName();
        String major = studentDetail.getMajor();
        String grade = studentDetail.getGrade();
        String intentionCity = studentDetail.getIntentionCity();
        String intentionIndustry = studentDetail.getIntentionIndustry();
        //在这里我引入了一个相关度的概念，Map中存储的是Long(StudentDetail的id)、Integer(与该学生的相关度)
        Map<Long,Integer> map = new HashMap<>();
        for (StudentDetail tempStudentDetail : studentDetailList){
            if (studentDetail.getStudentId().equals(tempStudentDetail.getStudentId())){
                continue;
            }
            int relevance = 0;//相关度
            String tempUniversityName = tempStudentDetail.getUniversityName();
            String tempMajor = tempStudentDetail.getMajor();
            String tempGrade = tempStudentDetail.getGrade();
            String tempIntentionCity = tempStudentDetail.getIntentionCity();
            String tempIntentionIndustry = tempStudentDetail.getIntentionIndustry();
            //1.先查找与他学校相同的人
            if (universityName.equals(tempUniversityName)){
                relevance += 4;
            }
            //2.查找与他专业相同的人
            if (major.equals(tempMajor)){
                relevance += 2;
            }
            //3.查找与他年级相同的人
            if (grade.equals(tempGrade)){
                relevance += 1;
            }
            //4.查找与他工作意向相同的人
            if (intentionCity.equals(tempIntentionCity)){
                relevance += 2;
            }
            //5.查找与他工作意向城市相同的人
            if (intentionIndustry.equals(tempIntentionIndustry)){
                relevance += 3;
            }
            map.put(tempStudentDetail.getStudentId(),relevance);
        }
        if (map.size() <= 9){
            for (Map.Entry<Long, Integer> entry : map.entrySet()) {
                StudentDetail tempStudentDetail = studentDetailRepository.findByStudentId(entry.getKey());
                tempStudentDetailList.add(tempStudentDetail);
            }
        }else {
            //把map按值也就是相关度的大小排序
            Set<Map.Entry<Long,Integer>> mapEntries = map.entrySet();
            List<Map.Entry<Long, Integer>> resultList = new LinkedList<>(mapEntries);
            resultList.sort(Comparator.comparing(Map.Entry::getValue));
            //因为排序结束是从小到大，所以从list的后面开始取
            for (int i = resultList.size(); i > 0; i--){
                if (tempStudentDetailList.size() > 9){
                    break;
                }
                Map.Entry<Long,Integer> newEntry = resultList.get(i - 1);
                StudentDetail tempStudentDetail = studentDetailRepository.findByStudentId(newEntry.getKey());
                tempStudentDetailList.add(tempStudentDetail);

            }
        }
        return tempStudentDetailList;
    }




}
