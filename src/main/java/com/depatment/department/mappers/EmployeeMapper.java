package com.depatment.department.mappers;

import com.depatment.department.models.Department_of_employee;
import com.depatment.department.models.Employee;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Mapper
public interface EmployeeMapper {


//--SELECT
    @Select("select \"First_name\",\"Last_name\"\n" +
            "from \"Department_of_employee\",\"Employee\" \n" +
            "where \"Department_of_employee\".\"Dep_id\"=#{dep_id}\n" +
            "and\n" +
            "\"Department_of_employee\".\"Empl_id\"=\"Employee\".\"Id\"")
    List<Employee> findEmpOfDepartment(@Param("dep_id") int id);

    @Select("select * from \"Employee\" where \"Id\"=#{id}")
    Employee findEmpById(@Param("id") int id);

    @Select("select \"First_name\",\"Last_name\"\n" +
            "from \"Department_of_employee\",\"Employee\" \n" +
            "where \"Dep_id\"=(select \"Dep_id\" from \"Department_of_employee\" where \"Empl_id\"=#{id} )\n" +
            "and\n" +
            "\"Department_of_employee\".\"Empl_id\"=\"Employee\".\"Id\"\n" +
            "and\n" +
            "\"Employee\".\"Is_boss\"=true")
    Employee findBossOfEmpl(@Param("id") int id);

    @Select("select \"Salary\"\n" +
            "from \"Department_of_employee\",\"Employee\" \n" +
            "where \"Dep_id\"=(select \"Dep_id\" from \"Department_of_employee\" where \"Empl_id\"=#{id} )\n" +
            "and\n" +
            "\"Department_of_employee\".\"Empl_id\"=\"Employee\".\"Id\"\n" +
            "and\n" +
            "\"Employee\".\"Is_boss\"=true")
    String findBossSalary(@Param("id") int id);

    @Select("select \"Employee\".\"Id\"\n" +
            "from \"Department_of_employee\",\"Employee\" \n" +
            "where \"Dep_id\"=(select \"Dep_id\" from \"Department_of_employee\" where \"Empl_id\"=#{id} )\n" +
            "and\n" +
            "\"Department_of_employee\".\"Empl_id\"=\"Employee\".\"Id\"\n" +
            "and\n" +
            "\"Employee\".\"Is_boss\"=true")
    Integer findBossId(@Param("id") int id);

    @Select("select * from \"Employee\" where \"Salary\">#{salary}")
    List<Employee> findEmplWithSalaryMoreThan(@Param("salary") int salary);

    @Select("select \"Hire_date\" from \"Employee\" where \"Id\"=#{id}")
    String findHireDate(@Param("id") int id);

    @Select("select \"Date_of_birth\" from \"Employee\" where \"Id\"=#{id}")
    String findDob(@Param("id") int id);

    @Select("select nextval('\"Employee_Id_seq\"')")
    Integer nextId();

    @Select("select \"Employee\".\"Id\"\n" +
            "from \"Department_of_employee\",\"Employee\" \n" +
            "where \"Dep_id\"=#{depid}\n" +
            "and\n" +
            "\"Department_of_employee\".\"Empl_id\"=\"Employee\".\"Id\"\n" +
            "and\n" +
            "\"Employee\".\"Is_boss\"=true")
    Integer findBossIdOfDepartment(@Param("depid") int depid);

    @Select("select \"Salary\"\n" +
            "from \"Department_of_employee\",\"Employee\" \n" +
            "where \"Dep_id\"=#{depid}\n" +
            "and\n" +
            "\"Department_of_employee\".\"Empl_id\"=\"Employee\".\"Id\"\n" +
            "and\n" +
            "\"Employee\".\"Is_boss\"=true")
    String findBossSalaryOfDepartment(@Param("depid") int depid);

//--INSERT
    @Insert("insert into \"Employee\" (\"Id\",\"First_name\",\"Last_name\",\"Patronymic\",\"Date_of_birth\",\"Email\",\"Hire_date\",\"Salary\",\"Is_boss\",\"Job_id\",\"Phone_number\")\n" +
            "values (default,#{first_name},#{last_name},#{patronymic}," +
            "to_date(#{date_of_birth},'YYYY-MM-DD')," +
            "#{email}," +
            "to_date(#{hire_date},'YYYY-MM-DD')," +
            "#{salary},#{is_boss},#{job_id},#{phone_number})")
    void insertEmployee(Employee employee);

    @Insert("insert into \"Department_of_employee\" (\"Dep_id\",\"Empl_id\") " +
            "values(#{dep_id},(select currval('\"Employee_Id_seq\"')))")
    void insertDepOfEmpl(Department_of_employee department_of_employee);


//--UPDATE
    @Update("update \"Employee\" set \"Retire_date\"=to_date(#{date},'YYYY-MM-DD') where \"Id\"=#{id}")
    void updateRetDate(@Param("id") int id,@Param("date") String date);

    @Update("update \"Department_of_employee\" set \"Dep_id\"=#{dep_id} where \"Empl_id\"=#{empl_id}")
    void updateDepForEmpl(@Param("empl_id") int emplid,@Param("dep_id") int depid);

    @Update("update \"Department_of_employee\" set \"Dep_id\"=#{transferto} where \"Dep_id\"=#{transferfrom}")
    void updateDepForEmpls(@Param("transferfrom")int fromdep,@Param("transferto") int todep);

    @Update("update \"Employee\" set \"First_name\"=#{first},\"Last_name\"=#{last},\"Patronymic\"=#{patr}," +
            "\"Email\"=#{email},\"Hire_date\"=to_date(#{hdate},'YYYY-MM-DD'),\"Salary\"=#{salary},\"Is_boss\"=#{boss},\"Job_id\"=#{jobid}," +
            "\"Phone_number\"=#{phone} where \"Id\"=#{id}")
    void updateEmpl(@Param("id") int id,@Param("first") String fname,@Param("last") String lname,
                    @Param("patr") String patr, @Param("email") String email,@Param("hdate") String hdate,
                    @Param("salary") int salary,@Param("boss")boolean bos,@Param("jobid") int jobid,@Param("phone") String phone);

    @Update("update \"Employee\" set \"Salary\"=#{salary} where \"Id\"=#{id}")
    void updateSalary(@Param("id")int id,@Param("salary") int salary);

    @Update("update \"Employee\" set \"Hire_date\"=to_date(#{hdate},'YYYY-MM-DD') where \"Id\"=#{id}")
    void updateHireDate(@Param("id")int id,@Param("hdate") String hdate);

    @Update("update \"Employee\" set \"Is_boss\"=#{isboss} where \"Id\"=#{id}")
    void updateIsBoss(@Param("id") int id,@Param("isboss") boolean isbos);

    @Update("update \"Employee\" set \"Is_boss\"=false where \"Id\" in (select \"Empl_id\" from \"Department_of_employee\" where \"Dep_id\"=#{depid})")
    void updateIsBossForAll(@Param("depid") int depid);


}
