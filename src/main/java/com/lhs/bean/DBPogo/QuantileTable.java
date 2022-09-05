package com.lhs.bean.DBPogo;

import com.alibaba.excel.annotation.ExcelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="quantile_table")   //样本置信度参照表
public class QuantileTable {
    @Id
    @ExcelProperty("区间")
    private Double section;
    @ExcelProperty("分位")
    private Double value;

    public Double getSection() {
        return section;
    }

    public void setSection(Double section) {
        this.section = section;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "QuantileTable{" +
                "section=" + section +
                ", value=" + value +
                '}';
    }
}
