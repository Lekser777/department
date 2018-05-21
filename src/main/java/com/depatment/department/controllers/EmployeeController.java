package com.depatment.department.controllers;

import com.depatment.department.mappers.EmployeeMapper;
import com.depatment.department.models.Department_of_employee;
import com.depatment.department.models.Employee;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeMapper employeeMapper;

    public EmployeeController(EmployeeMapper employeeMapper){
        this.employeeMapper=employeeMapper;
    }
    //E1
    @GetMapping("/ofdepartment")
    public List<Employee> getEmplOfDep(@RequestParam("depid") int depid){
        return employeeMapper.findEmpOfDepartment(depid);
    }

    //E5
    @GetMapping("/byid")
    public Employee getEmplById(@RequestParam("id") int id){
        return employeeMapper.findEmpById(id);
    }

    //E8
    @GetMapping("/boss")
    public Employee getBossOfEmpl(@RequestParam("id") int id){
        return employeeMapper.findBossOfEmpl(id);
    }

    //E9
    @GetMapping("/salarymorethan")
    public List<Employee> getEmplWithSalaryMoreThan(@RequestParam("salary") int salary){
        return employeeMapper.findEmplWithSalaryMoreThan(salary);
    }

    //E2
    @GetMapping("/add")
    public String addEmployee(@RequestParam("first") String firstname, @RequestParam("last") String lastname,
                              @RequestParam(value = "patr",defaultValue = "null") String patr,
                              @RequestParam("dob") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob,
                              @RequestParam("email") String email,
                              @RequestParam("hiredate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date hiredate,
                              @RequestParam("salary") int salary,@RequestParam("boss") boolean isboss,
                              @RequestParam("jobid") int jobid,@RequestParam("phone") String phone,
                              @RequestParam("depid") int depid){
        String mes="Успешное добавление";

        Employee employee=new Employee();
        employee.setFirst_name(firstname);
        employee.setLast_name(lastname);
        employee.setPatronymic(patr);
        employee.setDate_of_birth(dob);
        employee.setEmail(email);
        employee.setHire_date(hiredate);
        employee.setSalary(salary);
        employee.setJob_id(jobid);
        employee.setIs_boss(isboss);
        employee.setPhone_number(phone);

        Department_of_employee doe=new Department_of_employee();
        doe.setDep_id(depid);
        doe.setEmpl_id(employee.getId());
        try{
            employeeMapper.insertEmployee(employee);
            employeeMapper.insertDepOfEmpl(doe);
        }catch (SqlSessionException e){
            mes="Ошибка добавления";
        }
       return mes;
    }

    //E4
    @GetMapping("/retire")
    public String retireEmployee(@RequestParam("id") int id, @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd") Date date){

        String mess="Сотрудник успешно уволен";
        try{employeeMapper.updateRetDate(id,date);}
        catch (SqlSessionException e){
            mess="Ошибка при увольнение сотрудника.";
        }
        return mess;
    }

    //E6
    @GetMapping("/transfer")
    public String transferEmployee(@RequestParam("emplid") int emplid, @RequestParam("depid")int depid){

        String mess="Сотрудник успешно переведен";
        try{employeeMapper.updateDepForEmpl(emplid,depid);}
        catch (SqlSessionException e){
            mess="Ошибка при переводе сотрудника.";
        }
        return mess;
    }

    //E7
    @GetMapping("/transfer/all")
    public String transferEmployees(@RequestParam("from") int fromdep, @RequestParam("to")int todep){

        String mess="Все сотрудники успешно переведены";
        try{employeeMapper.updateDepForEmpls(fromdep,todep);}
        catch (SqlSessionException e){
            mess="Ошибка при переводе сотрудников.";
        }
        return mess;
    }

    //E3//насчет босса продумать
    @GetMapping("/update")
    public String transferEmployees(@RequestParam("id") int id, @RequestParam("fname")String fname,@RequestParam("lname")String lname,
                                    @RequestParam("patr") String patr,@RequestParam("hdate")@DateTimeFormat(pattern = "yyyy-MM-dd") Date hdate,
                                    @RequestParam("email") String email, @RequestParam("sal") int sal,@RequestParam("boss") boolean boss,
                                    @RequestParam("jobid") int jobid,@RequestParam("phone") String phone){

        String mess="Информация о сотруднике успешно обновлена";
        try{employeeMapper.updateEmpl(id,fname,lname,patr,email,hdate,sal,boss,jobid,phone);}
        catch (SqlSessionException e){
            mess="Ошибка при переводе сотрудников.";
        }
        return mess;



    }



}
