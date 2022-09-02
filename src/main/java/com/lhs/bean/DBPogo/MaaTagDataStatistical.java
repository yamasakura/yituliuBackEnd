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
    private    Long id;
    private  int topOperator ;  //高级资深总数
    private  int seniorOperator; //资深总数
    private  int topAndSeniorOperator; //高级资深含有资深总数
    private  int seniorOperatorCount;  //五星TAG总数
    private  int rareOperatorCount;   //四星TAG总数
    private  int commonOperatorCount; //三星TAG总数
    private  int robot;                //小车TAG总数
    private   int robotChoice;       //小车和其他组合共同出现次数
    private   int vulcan;             //火神出现次数
    private int gravel;         //砾出现次数
    private int specialist;     //特种出现次数
    private int jessica;    //杰西卡次数
    private   double MaaTagsDataCount; //总数据量
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTopOperator() {
        return topOperator;
    }

    public void setTopOperator(int topOperator) {
        this.topOperator = topOperator;
    }

    public int getSeniorOperator() {
        return seniorOperator;
    }

    public void setSeniorOperator(int seniorOperator) {
        this.seniorOperator = seniorOperator;
    }

    public int getTopAndSeniorOperator() {
        return topAndSeniorOperator;
    }

    public void setTopAndSeniorOperator(int topAndSeniorOperator) {
        this.topAndSeniorOperator = topAndSeniorOperator;
    }

    public int getSeniorOperatorCount() {
        return seniorOperatorCount;
    }

    public void setSeniorOperatorCount(int seniorOperatorCount) {
        this.seniorOperatorCount = seniorOperatorCount;
    }

    public int getRareOperatorCount() {
        return rareOperatorCount;
    }

    public void setRareOperatorCount(int rareOperatorCount) {
        this.rareOperatorCount = rareOperatorCount;
    }

    public int getCommonOperatorCount() {
        return commonOperatorCount;
    }

    public void setCommonOperatorCount(int commonOperatorCount) {
        this.commonOperatorCount = commonOperatorCount;
    }

    public int getRobot() {
        return robot;
    }

    public void setRobot(int robot) {
        this.robot = robot;
    }

    public int getRobotChoice() {
        return robotChoice;
    }

    public void setRobotChoice(int robotChoice) {
        this.robotChoice = robotChoice;
    }

    public int getVulcan() {
        return vulcan;
    }

    public void setVulcan(int vulcan) {
        this.vulcan = vulcan;
    }

    public int getGravel() {
        return gravel;
    }

    public void setGravel(int gravel) {
        this.gravel = gravel;
    }

    public int getSpecialist() {
        return specialist;
    }

    public void setSpecialist(int specialist) {
        this.specialist = specialist;
    }

    public int getJessica() {
        return jessica;
    }

    public void setJessica(int jessica) {
        this.jessica = jessica;
    }

    public double getMaaTagsDataCount() {
        return MaaTagsDataCount;
    }

    public void setMaaTagsDataCount(double maaTagsDataCount) {
        MaaTagsDataCount = maaTagsDataCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
