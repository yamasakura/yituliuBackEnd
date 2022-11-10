package com.lhs.dao;

import com.lhs.bean.DBPogo.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface StageDao extends JpaRepository<Stage, Long>{

    @Transactional
    @Modifying
    @Query(value = "UPDATE stage SET is_show =?1 WHERE stage_id = ?2",nativeQuery = true)
    void updateStageInfo(Integer isShow, String stageId);  //控制关卡是否可被查出

}
