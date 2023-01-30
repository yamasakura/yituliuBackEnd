package com.lhs.bean.DBPogo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pool_data")
public class PoolData {

    @Id
    private Long id;
    private String uid;
    private String pool;
    private Integer poolType;
    private Long ts;
    private String name;
    private Integer rarity;
    private Boolean isNew;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

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

    public Boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public Integer getPoolType() {
        return poolType;
    }

    public void setPoolType(Integer poolType) {
        this.poolType = poolType;
    }

    public Boolean getNew() {
        return isNew;
    }

    public void setNew(Boolean aNew) {
        isNew = aNew;
    }

    @Override
    public String toString() {
        return "PoolData{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", pool='" + pool + '\'' +
                ", poolType='" + poolType + '\'' +
                ", ts=" + ts +
                ", name='" + name + '\'' +
                ", rarity=" + rarity +
                ", isNew=" + isNew +
                '}';
    }
}
