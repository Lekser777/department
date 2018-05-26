package com.depatment.department.models;

import org.apache.ibatis.annotations.Insert;

public class SalaryFunds {
    Integer id;
    Integer dep_id;
    Integer salary_of_dep;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDep_id() {
        return dep_id;
    }

    public void setDep_id(Integer dep_id) {
        this.dep_id = dep_id;
    }

    public Integer getSalary_of_dep() {
        return salary_of_dep;
    }

    public void setSalary_of_dep(Integer salary_of_dep) {
        this.salary_of_dep = salary_of_dep;
    }
}
