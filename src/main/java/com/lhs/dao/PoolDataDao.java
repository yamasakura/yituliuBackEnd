package com.lhs.dao;


import com.lhs.bean.DBPogo.PoolData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PoolDataDao extends JpaRepository<PoolData, Long> {
       List<PoolData> findByUid(String uid);
}

