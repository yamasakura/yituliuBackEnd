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
public class Visits implements Serializable {


    @Id
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date visitsDay;

    private Integer visits;

    private Integer  visitsOld;

    private Integer  visitsNew;

    private Integer  visitsBot;

    public Date getVisitsDay() {
        return visitsDay;
    }

    public void setVisitsDay(Date day) {
        this.visitsDay = day;
    }

    public Integer getVisits() {
        return visits;
    }

    public void setVisits(Integer visits) {
        this.visits = visits;
    }

    public Integer getVisitsOld() {
        return visitsOld;
    }

    public void setVisitsOld(Integer visitsOld) {
        this.visitsOld = visitsOld;
    }

    public Integer getVisitsNew() {
        return visitsNew;
    }

    public void setVisitsNew(Integer visitsNew) {
        this.visitsNew = visitsNew;
    }

    public Integer getVisitsBot() {
        return visitsBot;
    }

    public void setVisitsBot(Integer visitsBot) {
        this.visitsBot = visitsBot;
    }

    @Override
    public String toString() {
        return "Visits{" +
                "visitsDay='" + visitsDay + '\'' +
                ", visits=" + visits +
                ", visitsNew=" + visitsNew +
                '}';
    }


}
