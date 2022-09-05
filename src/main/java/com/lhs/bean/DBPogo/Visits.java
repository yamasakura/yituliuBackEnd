package com.lhs.bean.DBPogo;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="visits")
public class Visits {


    @Id
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date visitsDay;
    private Integer visits;

    public Date getVisitsDay() {
        return visitsDay;
    }

    public void setVisitsDay(Date visitsDay) {
        this.visitsDay = visitsDay;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    @Override
    public String toString() {
        return "Visits{" +
                "visitsDay=" + visitsDay +
                ", visits=" + visits +
                '}';
    }
}
