package com.lhs.dao;

import com.lhs.bean.DBPogo.BuildingSchedule;
import com.lhs.bean.DBPogo.IpData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface ScheduleDao extends JpaRepository<BuildingSchedule, Long> {



}
