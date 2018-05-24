package com.depatment.department.models;

public class DepartmentWithInfo {
    private String name;
    private String creation_date;
    private Integer id;
    private String first_name;
    private String last_name;
    private Integer count;

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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
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
