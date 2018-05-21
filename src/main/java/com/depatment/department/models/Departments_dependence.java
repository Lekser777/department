package com.depatment.department.models;

public class Departments_dependence {
    private Integer id;
    private Integer main_dep_id;
    private Integer linked_to_dep_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMain_dep_id() {
        return main_dep_id;
    }

    public void setMain_dep_id(Integer main_dep_id) {
        this.main_dep_id = main_dep_id;
    }

    public Integer getLinked_to_dep_id() {
        return linked_to_dep_id;
    }

    public void setLinked_to_dep_id(Integer linked_to_dep_id) {
        this.linked_to_dep_id = linked_to_dep_id;
    }
}
