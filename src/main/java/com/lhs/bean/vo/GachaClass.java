package com.lhs.bean.vo;


import java.util.List;


public class GachaClass {


    private Long ts;

    private String pool;

    private List<Chars> chars;

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

    public List<Chars> getChars() {
        return chars;
    }

    public void setChars(List<Chars> chars) {
        this.chars = chars;
    }

    @Override
    public String toString() {
        return "GachaClass{" +
                "ts='" + ts + '\'' +
                ", pool='" + pool + '\'' +
                ", chars=" + chars +
                '}';
    }







}