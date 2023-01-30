package com.lhs.bean.DBPogo;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;



public class FeedBack {

    private String description;
    private String scheduleId;
    private Date createdTime;
    private String imageUrl;
    private Integer closed;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getClosed() {
        return closed;
    }

    public void setClosed(Integer closed) {
        this.closed = closed;
    }

    @Override
    public String toString() {
        return "FeedBack{" +
                "description='" + description + '\'' +
                ", scheduleId='" + scheduleId + '\'' +
                ", createdTime=" + createdTime +
                ", imageUrl='" + imageUrl + '\'' +
                ", closed=" + closed +
                '}';
    }
}
