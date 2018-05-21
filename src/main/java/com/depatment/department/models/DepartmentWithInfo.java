package com.depatment.department.models;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class DepartmentWithInfo {
    private String name;
    private Date creation_date;
    private Integer id;
    private String firstt_name;
    private String last_name;
    private Integer count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(Date creation_date) {
        this.creation_date = creation_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstt_name() {
        return firstt_name;
    }

    public void setFirstt_name(String firstt_name) {
        this.firstt_name = firstt_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
