package com.lhs.bean.vo;

public class Chars {
    private String name;
    private Integer rarity;
    private boolean isNew;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRarity() {
        return rarity;
    }

    public void setRarity(Integer rarity) {
        this.rarity = rarity;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    @Override
    public String toString() {
        return "Chars{" +
                "name='" + name + '\'' +
                ", rarity=" + rarity +
                ", isNew=" + isNew +
                '}';
    }
}
