package com.lhs.dao;

import com.lhs.bean.DBPogo.MaaTagData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


public interface MaaTagResultDao extends JpaRepository<MaaTagData, Long>{

    @Transactional
    @Modifying
    @Query(value = "select * from maa_tag_data order by maa_tag_data.create_time  desc limit 10",nativeQuery = true)
    List<MaaTagData> selectDataLimit10();

    List<MaaTagData> findByCreateTimeIsGreaterThanEqualAndCreateTimeIsLessThan(Date createTime, Date createTime2);

    List<MaaTagData> findByCreateTimeIsLessThan(Date createTime);
}