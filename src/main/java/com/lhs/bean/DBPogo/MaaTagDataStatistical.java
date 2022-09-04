package com.lhs.bean.DBPogo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Entity
@Table(name = "maa_tag_data_statistical")
public class MaaTagDataStatistical {

    @Id
    private  Long id;
    private  Integer topOperator ;  //高级资深总数
    private  Integer seniorOperator; //资深总数
    private  Integer topAndSeniorOperator; //高级资深含有资深总数
    private  Integer seniorOperatorCount;  //五星TAG总数
    private  Integer rareOperatorCount;   //四星TAG总数
    private  Integer commonOperatorCount; //三星TAG总数
    private  Integer robot;                //小车TAG总数
    private  Integer robotChoice;       //小车和其他组合共同出现次数
    private  Integer vulcan;             //火神出现次数
    private  Integer gravel;         //砾出现次数
    private  Integer jessica;    //杰西卡次数
    private  Integer MaaTagsDataCount; //总数据量
    private Date lastTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTopOperator() {
        return topOperator;
    }

    public void setTopOperator(Integer topOperator) {
        this.topOperator = topOperator;
    }

    public Integer getSeniorOperator() {
        return seniorOperator;
    }

    public void setSeniorOperator(Integer seniorOperator) {
        this.seniorOperator = seniorOperator;
    }

    public Integer getTopAndSeniorOperator() {
        return topAndSeniorOperator;
    }

    public void setTopAndSeniorOperator(Integer topAndSeniorOperator) {
        this.topAndSeniorOperator = topAndSeniorOperator;
    }

    public Integer getSeniorOperatorCount() {
        return seniorOperatorCount;
    }

    public void setSeniorOperatorCount(Integer seniorOperatorCount) {
        this.seniorOperatorCount = seniorOperatorCount;
    }

    public Integer getRareOperatorCount() {
        return rareOperatorCount;
    }

    public void setRareOperatorCount(Integer rareOperatorCount) {
        this.rareOperatorCount = rareOperatorCount;
    }

    public Integer getCommonOperatorCount() {
        return commonOperatorCount;
    }

    public void setCommonOperatorCount(Integer commonOperatorCount) {
        this.commonOperatorCount = commonOperatorCount;
    }

    public Integer getRobot() {
        return robot;
    }

    public void setRobot(Integer robot) {
        this.robot = robot;
    }

    public Integer getRobotChoice() {
        return robotChoice;
    }

    public void setRobotChoice(Integer robotChoice) {
        this.robotChoice = robotChoice;
    }

    public Integer getVulcan() {
        return vulcan;
    }

    public void setVulcan(Integer vulcan) {
        this.vulcan = vulcan;
    }

    public Integer getGravel() {
        return gravel;
    }

    public void setGravel(Integer gravel) {
        this.gravel = gravel;
    }


    public Integer getJessica() {
        return jessica;
    }

    public void setJessica(Integer jessica) {
        this.jessica = jessica;
    }

    public Integer getMaaTagsDataCount() {
        return MaaTagsDataCount;
    }

    public void setMaaTagsDataCount(Integer maaTagsDataCount) {
        MaaTagsDataCount = maaTagsDataCount;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
