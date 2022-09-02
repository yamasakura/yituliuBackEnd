package com.lhs.bean.DBPogo;

import com.alibaba.excel.annotation.ExcelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "char_tag_data")
public class CharTagData implements Serializable {
    private static final long serialVersionUID = 1L;


    @Id
    @ExcelProperty("序号")
    private Integer id;
    @ExcelProperty("干员")
    private String roleName;
    @ExcelProperty("可否招募")
    private Integer type;
    @ExcelProperty("星级")
    private Integer rarity;
    @ExcelProperty("干员级别")
    private String grade;
    @ExcelProperty("站位")
    private String position;
    @ExcelProperty("职业")
    private String occupation;
    @ExcelProperty("词条1")
    private String tag1;
    @ExcelProperty("词条2")
    private String tag2;
    @ExcelProperty("词条3")
    private String tag3;
    @ExcelProperty("词条值")
    private String tagValue;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getRarity() {
        return rarity;
    }

    public void setRarity(Integer rarity) {
        this.rarity = rarity;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    @Override
    public String toString() {
        return "CharTagData{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", type=" + type +
                ", rarity=" + rarity +
                ", grade='" + grade + '\'' +
                ", position='" + position + '\'' +
                ", occupation='" + occupation + '\'' +
                ", tag1='" + tag1 + '\'' +
                ", tag2='" + tag2 + '\'' +
                ", tag3='" + tag3 + '\'' +
                ", tagValue='" + tagValue + '\'' +
                '}';
    }
}
