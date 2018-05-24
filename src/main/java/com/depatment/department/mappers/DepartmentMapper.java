package com.depatment.department.mappers;

import com.depatment.department.models.Department;
import com.depatment.department.models.DepartmentWithInfo;
import com.depatment.department.models.Departments_dependence;
import com.depatment.department.models.SalaryFund;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface DepartmentMapper {
    @Select("select * from \"Department\"")
    List<Department> findAll();
    @Select("select distinct a.\"Name\",a.\"Creation_date\",a.\"First_name\",a.\"Last_name\",b.count " +
            "from(select \"Department\".\"Name\",\"Department\".\"Creation_date\",\"First_name\",\"Last_name\" from \"Department\",\"Employee\",\"Department_of_employee\" where (\"Department\".\"Id\"=\"Dep_id\" and \"Employee\".\"Id\" =\"Empl_id\" and \"Is_boss\"=true and \"Department\".\"Id\"=#{id})) as a,(select \"Dep_id\",count(\"Empl_id\") from \"Department_of_employee\" group by(\"Dep_id\")) as b")
    DepartmentWithInfo findById(@Param("id") int id);
    @Select("select \"Department\".\"Name\",\"Department\".\"Creation_date\" from \"Departments_dependence\",\"Department\" where \"Departments_dependence\".\"Main_dep_id\"=#{id} and \"Department\".\"Id\"=\"Departments_dependence\".\"Linked_to_dep_id\"")
    List<Department> findAllLowerByLvl(@Param("id") int id);
    @Select("with recursive tmp as(\n" +
            "select \"Departments_dependence\".\"Main_dep_id\",\"Departments_dependence\".\"Linked_to_dep_id\" \n" +
            "from \"Departments_dependence\"\n" +
            "where \"Departments_dependence\".\"Main_dep_id\"=#{id} \n" +
            "\n" +
            "union\n" +
            "\n" +
            "select \"Departments_dependence\".\"Main_dep_id\",\"Departments_dependence\".\"Linked_to_dep_id\" \n" +
            "from \"Departments_dependence\"\n" +
            "join tmp \n" +
            "on \"Departments_dependence\".\"Main_dep_id\"=tmp.\"Linked_to_dep_id\"\n" +
            ")\n" +
            "select \"Name\",\"Creation_date\" from tmp,\"Department\"\n" +
            "where \"Department\".\"Id\"=tmp.\"Linked_to_dep_id\";")
    List<Department> findAllLowerByAllLvl(@Param("id") int id);

    @Select("with recursive tmp as(\n" +
            "select \"Departments_dependence\".\"Main_dep_id\",\"Departments_dependence\".\"Linked_to_dep_id\" \n" +
            "from \"Departments_dependence\"\n" +
            "where \"Departments_dependence\".\"Linked_to_dep_id\"=#{id} \n" +
            "\n" +
            "union\n" +
            "\n" +
            "select \"Departments_dependence\".\"Main_dep_id\",\"Departments_dependence\".\"Linked_to_dep_id\" \n" +
            "from \"Departments_dependence\"\n" +
            "join tmp \n" +
            "on \"Departments_dependence\".\"Linked_to_dep_id\"=tmp.\"Main_dep_id\"\n" +
            ")\n" +
            "select \"Name\",\"Creation_date\" from tmp,\"Department\"\n" +
            "where \"Department\".\"Id\"=tmp.\"Main_dep_id\";")
    List<Department> findAllHigherByAllLvl(@Param("id")int id);

    @Select("select \"Name\",\"Creation_date\" from \"Department\" where \"Name\"=#{name}")
    Department findByName(@Param("name") String name);

    @Select("select sum(\"Salary\") \n" +
            "from \"Department_of_employee\",\"Employee\" \n" +
            "where \"Department_of_employee\".\"Dep_id\"=#{id} \n" +
            "and\n" +
            "\"Department_of_employee\".\"Empl_id\"=\"Employee\".\"Id\"")
    SalaryFund findFundByid(@Param("id") int id);


    @Insert("INSERT INTO \"Department\"(\"Id\",\"Name\",\"Creation_date\") VALUES (DEFAULT,#{name} ,to_date(#{creation_date},'YYYY-MM-DD'))")
    void insertDepartment(Department department);
    @Insert("INSERT INTO \"Departments_dependence\"(\"Main_dep_id\",\"Linked_to_dep_id\") VALUES (#{main_dep_id},(select currval('\"Department_Id_seq\"')));")
    void insertDepartment_dep(Departments_dependence departments_dependence);



    @Update("update \"Department\" set \"Name\"=#{newname} where \"Name\"=#{curname}")
    void updateDepName(@Param("curname") String curname,@Param("newname") String newname);

    @Update("update \"Departments_dependence\" set \"Main_dep_id\"=#{linked_to_id} where \"Linked_to_dep_id\"=#{dep_id}")
    void updateDependency(@Param("dep_id") int depid,@Param("linked_to_id") int linkedid);


    @Delete("Delete from \"Department\" where \"Name\"=#{name}")
    void deleteDep(@Param("name") String name);

}
