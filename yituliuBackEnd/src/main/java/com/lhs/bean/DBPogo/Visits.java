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
    private Integer visitsBot;
    private Integer visitsBuilding;
    private Integer visitsIndex;
    private Integer visitsGacha;

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

    public Integer getVisitsBot() {
        return visitsBot;
    }

    public void setVisitsBot(Integer visitsBot) {
        this.visitsBot = visitsBot;
    }

    public Integer getVisitsBuilding() {
        return visitsBuilding;
    }

    public void setVisitsBuilding(Integer visitsBuilding) {
        this.visitsBuilding = visitsBuilding;
    }

    public Integer getVisitsIndex() {
        return visitsIndex;
    }

    public void setVisitsIndex(Integer visitsIndex) {
        this.visitsIndex = visitsIndex;
    }

    public Integer getVisitsGacha() {
        return visitsGacha;
    }

    public void setVisitsGacha(Integer visitsGacha) {
        this.visitsGacha = visitsGacha;
    }

    @Override
    public String toString() {
        return "Visits{" +
                "visitsDay=" + visitsDay +
                ", visits=" + visits +
                ", visitsBot=" + visitsBot +
                ", visitsBuilding=" + visitsBuilding +
                ", visitsIndex=" + visitsIndex +
                ", visitsGacha=" + visitsGacha +
                '}';
    }
}
