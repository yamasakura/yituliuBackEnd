package com.lhs.dao;


import com.lhs.bean.vo.StageResultApiVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StageResultVoApiDao extends JpaRepository<StageResultApiVo, Long> {

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE stage_result_api_vo",nativeQuery = true)
    void deleteQuery();


    //根据物品类别查询效率升序
    Page<StageResultApiVo> findByTypeAndEfficiencyGreaterThanAndTimesGreaterThanOrderByEfficiencyDesc(String type, Double efficiency, Integer times, Pageable pageable);
    //查询所有关卡信息
    List<StageResultApiVo> findByTypeNotNullAndEfficiencyLessThanOrderByEfficiencyDesc(Double efficiency);


}
