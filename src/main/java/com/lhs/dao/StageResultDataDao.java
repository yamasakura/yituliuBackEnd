package com.lhs.dao;


import com.lhs.bean.DBPogo.StageResultData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StageResultDataDao extends JpaRepository<StageResultData, Long> {

    //根据活动名称前缀如act12_side，效率降序
    List<StageResultData> findByZoneIdAndVersionAndMainLevelGreaterThanAndMainIsNotNullOrderByCodeAsc(String chapterName,Double version, Integer mainLevel);

    //根据物品类别查询，效率降序
    List<StageResultData> findByItemTypeAndIsShowAndVersionAndEfficiencyGreaterThanAndSampleSizeGreaterThanOrderByEfficiencyExDesc(
            String itemType, Integer isShow,Double version, Double efficiency, Integer times);
    //根据物品类别查询，效率降序
    List<StageResultData> findByItemTypeAndIsShowAndVersionAndEfficiencyGreaterThanAndSampleSizeGreaterThanOrderByEfficiencyDesc(
            String itemType, Integer isShow,Double version, Double efficiency, Integer times);

    //根据物品名称查询，期望理智升序
    List<StageResultData> findByItemNameAndIsShowAndVersionAndApExpectLessThanAndSampleSizeGreaterThanOrderByApExpectAsc(
            String type, Integer isShow,Double version, Double expect, Integer times);

    //根据关卡id查询，效率降序
    List<StageResultData> findByStageIdAndIsShow(String stageId, Integer isShow);

    //根据物品类别查询，关卡效率降序
    Page<StageResultData> findByItemTypeAndIsShowAndEfficiencyGreaterThanAndSampleSizeGreaterThanOrderByEfficiencyDesc(
            String type, Integer isShow, Double efficiency, Integer times, Pageable pageable);

    //根据物品名称查询，期望理智升序
    Page<StageResultData> findByItemNameAndIsShowAndApExpectLessThanAndSampleSizeGreaterThanOrderByApExpectAsc(
            String itemName, Integer isShow, Double expect, Integer times, Pageable pageable);

    //查询所有关卡信息
    List<StageResultData> findByItemTypeNotNullAndEfficiencyLessThanOrderByEfficiencyDesc(Double efficiency);


}
