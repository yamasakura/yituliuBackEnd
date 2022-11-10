package com.lhs.bean.vo;

import java.util.List;

public class RecResultVo {

    private  String tags;
    private List<Object> result;
    private String lessRarity;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<Object> getResult() {
        return result;
    }

    public void setResult(List<Object> result) {
        this.result = result;
    }

    public String getLessRarity() {
        return lessRarity;
    }

    public void setLessRarity(String lessRarity) {
        this.lessRarity = lessRarity;
    }

    public RecResultVo() {
    }

    public RecResultVo(String tags, List<Object> result, String lessRarity) {
        this.tags = tags;
        this.result = result;
        this.lessRarity = lessRarity;
    }

    @Override
    public String toString() {
        return "RecResultVo{" +
                "tags='" + tags + '\'' +
                ", result=" + result +
                ", lessRarity='" + lessRarity + '\'' +
                '}';
    }
}
