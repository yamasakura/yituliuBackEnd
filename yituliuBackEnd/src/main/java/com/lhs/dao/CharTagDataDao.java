package com.lhs.dao;


import com.lhs.bean.DBPogo.CharTagData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharTagDataDao extends JpaRepository<CharTagData, Long> {

    //查询干员tag信息  type为是否可招募，1为可招募
    List<CharTagData> findByTypeAndRarityBetween(Integer type, Integer rarityMin, Integer rarityMax);
}
