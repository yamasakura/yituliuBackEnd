package com.lhs.dao;


import com.lhs.bean.DBPogo.MaaTagDataStatistical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MaaTagDataStatisticalDao extends JpaRepository<MaaTagDataStatistical, Long> {

    @Transactional
    @Modifying
    @Query(value = "select * from maa_tag_data_statistical202301 order by maa_tag_data_statistical202301.last_time  desc limit 2",nativeQuery = true)
    List<MaaTagDataStatistical> getMaaTagDataStatistical();




}
