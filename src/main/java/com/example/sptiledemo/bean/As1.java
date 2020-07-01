package com.example.sptiledemo.bean;

import java.util.List;

public class As1 {
    private String name;
    private Integer rows;
    private List<As2> as2s;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<As2> getAs2s() {
        return as2s;
    }

    public void setAs2s(List<As2> as2s) {
        this.as2s = as2s;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
