package com.depatment.department.models;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Department {
    private String name;

    private String creation_date;

    private Integer id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
