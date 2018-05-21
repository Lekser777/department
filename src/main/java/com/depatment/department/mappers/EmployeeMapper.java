package com.depatment.department.mappers;

import com.depatment.department.models.Department_of_employee;
import com.depatment.department.models.Employee;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface EmployeeMapper {
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

    @Select("select * from \"Employee\" where \"Salary\">#{salary}")
    List<Employee> findEmplWithSalaryMoreThan(@Param("salary") int salary);

    @Insert("insert into \"Employee\" (\"Id\",\"First_name\",\"Last_name\",\"Patronymic\",\"Date_of_birth\",\"Email\",\"Hire_date\",\"Salary\",\"Is_boss\",\"Job_id\",\"Phone_number\")\n" +
            "values (default,#{first_name},#{last_name},#{patronymic}," +
            "#{date_of_birth}," +
            "#{email}," +
            "#{hire_date}," +
            "#{salary},#{is_boss},#{job_id},#{phone_number})")
    void insertEmployee(Employee employee);

    @Insert("insert into \"Department_of_employee\" (\"Dep_id\",\"Empl_id\") " +
            "values(#{dep_id},(select currval('\"Employee_Id_seq\"')))")
    void insertDepOfEmpl(Department_of_employee department_of_employee);

    @Update("update \"Employee\" set \"Retire_date\"=#{date} where \"Id\"=#{id}")
    void updateRetDate(@Param("id") int id,@Param("date") Date date);

    @Update("update \"Department_of_employee\" set \"Dep_id\"=#{dep_id} where \"Empl_id\"=#{empl_id}")
    void updateDepForEmpl(@Param("empl_id") int emplid,@Param("dep_id") int depid);

    @Update("update \"Department_of_employee\" set \"Dep_id\"=#{transferto} where \"Dep_id\"=#{transferfrom}")
    void updateDepForEmpls(@Param("transferfrom")int fromdep,@Param("transferto") int todep);

    @Update("update \"Employee\" set \"First_name\"=#{first},\"Last_name\"=#{last},\"Patronymic\"=#{patr}," +
            "\"Email\"=#{email},\"Hire_date\"=#{hdate},\"Salary\"=#{salary},\"Is_boss\"=#{boss},\"Job_id\"=#{jobid}," +
            "\"Phone_number\"=#{phone} where \"Id\"=#{id}")
    void updateEmpl(@Param("id") int id,@Param("first") String fname,@Param("last") String lname,
                    @Param("patr") String patr, @Param("email") String email,@Param("hdate") Date hdate,
                    @Param("salary") int salary,@Param("boss")boolean bos,@Param("jobid") int jobid,@Param("phone") String phone);

}
