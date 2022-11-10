package com.lhs.dao;

import com.lhs.bean.DBPogo.IpData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface IpDataDao extends JpaRepository<IpData, Long> {

    IpData findIpDataByIpAndVisitDayAndDomainName(String ip, Date visitTime, String domainName);


}
