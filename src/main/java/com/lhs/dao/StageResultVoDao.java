package com.lhs.dao;


import com.lhs.bean.vo.StageResultVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StageResultVoDao extends JpaRepository<StageResultVo, Long> {

    //根据活动名称前缀如act12_side，效率降序
    List<StageResultVo> findByChapterNameAndMainLevelGreaterThanAndMainIsNotNullOrderByCodeAsc(String chapterName, Integer mainLevel);

    //根据物品类别查询，效率降序
    List<StageResultVo> findByTypeAndIsShowAndEfficiencyGreaterThanAndTimesGreaterThanOrderByEfficiencyDesc(String type, Integer isShow, Double efficiency, Integer times);

    //根据物品名称查询，期望理智升序
    List<StageResultVo> findByItemNameAndIsShowAndExpectLessThanAndTimesGreaterThanOrderByExpectAsc(String type, Integer isShow, Double expect, Integer times);


    //根据物品类别查询，关卡效率降序
    Page<StageResultVo> findByTypeAndIsShowAndEfficiencyGreaterThanAndTimesGreaterThanOrderByEfficiencyDesc(String type, Integer isShow, Double efficiency, Integer times, Pageable pageable);

    //根据物品名称查询，期望理智升序
    Page<StageResultVo> findByItemNameAndIsShowAndExpectLessThanAndTimesGreaterThanOrderByExpectAsc(String itemName, Integer isShow,  Double expect, Integer times, Pageable pageable);


    //查询所有关卡信息
    List<StageResultVo> findByTypeNotNullAndEfficiencyLessThanOrderByEfficiencyDesc(Double efficiency);


}
