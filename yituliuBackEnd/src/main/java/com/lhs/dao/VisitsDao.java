package com.lhs.dao;


import com.lhs.bean.DBPogo.Visits;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import java.util.List;

public interface VisitsDao extends JpaRepository<Visits, Long> {


    Visits findVisitsByVisitsDay(Date day);

    List<Visits> findByVisitsDayGreaterThanEqualAndVisitsDayLessThanEqual(Date start, Date end);
}
