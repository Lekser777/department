package com.depatment.department.controllers;

import com.depatment.department.mappers.EmployeeMapper;
import com.depatment.department.models.Department;
import com.depatment.department.models.Department_of_employee;
import com.depatment.department.models.Departments_dependence;
import com.depatment.department.models.Employee;
import com.depatment.department.validator.DateValidator;
import com.depatment.department.validator.EmployeeValidator;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeMapper employeeMapper;

    private final EmployeeValidator employeeValidator;

    private final DateValidator dateValidator;

    public EmployeeController(EmployeeMapper employeeMapper, EmployeeValidator employeeValidator, DateValidator dateValidator){
        this.employeeMapper=employeeMapper;
        this.employeeValidator=employeeValidator;
        this.dateValidator=dateValidator;
    }
    //E1
    @GetMapping("/find/ofdepartment")
    public List<Employee> getEmplOfDep(@RequestParam("depid") int depid){
        return employeeMapper.findEmpOfDepartment(depid);
    }

    //E5
    @GetMapping("/find/byid")
    public Employee getEmplById(@RequestParam("id") int id){
        return employeeMapper.findEmpById(id);
    }

    //E8
    @GetMapping("/find/boss")
    public Employee getBossOfEmpl(@RequestParam("id") int id){
        return employeeMapper.findBossOfEmpl(id);
    }

    //E9
    @GetMapping("/find/salarymorethan")
    public List<Employee> getEmplWithSalaryMoreThan(@RequestParam("salary") int salary){
        return employeeMapper.findEmplWithSalaryMoreThan(salary);
    }
    @GetMapping("/find/hdateof")
    public String findEmplHDate(@RequestParam("id") int id){
        return employeeMapper.findHireDate(id);
    }
    @GetMapping("/find/dobof")
    public String findEmplDob(@RequestParam("id") int id){
        return employeeMapper.findDob(id);
    }

    //E2
    @PostMapping("/add")
    public String addEmployee(@RequestBody Employee employee,@RequestParam("depid") int depid){

        String mes="";

        mes=employeeValidator.IsEmployeeValid(depid,employee.getFirst_name(),employee.getLast_name(),employee.getPatronymic(),
                employee.getDate_of_birth(),employee.getHire_date(),employee.getEmail()
                ,employee.getSalary().toString(),employee.getPhone_number(),employee.getIs_boss());
        if(mes.equals("OK")) {
            Department_of_employee doe = new Department_of_employee();
            doe.setDep_id(depid);
            doe.setEmpl_id(employee.getId());
            try {
                employeeMapper.insertEmployee(employee);
                employeeMapper.insertDepOfEmpl(doe);
            } catch (RuntimeException e) {
                mes=mes+" PSQLException";
            }
        }
        return mes;
    }

    //E4
    @PatchMapping("/retire")
    public String retireEmployee(@RequestParam("id") int id, @RequestBody Employee employee){

        String mess="Сотрудник успешно уволен";
        try{
            if(dateValidator.IsFirstDateMore(employee.getRetire_date(),employeeMapper.findHireDate(id))){
                employeeMapper.updateRetDate(id,employee.getRetire_date());
                employeeMapper.updateIsBoss(id,false);
            }else {
                mess="Не верно введена дата";
            }
        }
        catch (RuntimeException e){
            mess="Ошибка при увольнение сотрудника.";
        }
        return mess;
    }

    //E6
    @PatchMapping("/transfer")
    public String transferEmployee(@RequestParam("emplid") int emplid,@RequestBody Department_of_employee department_of_employee){

        String mess="Сотрудник успешно переведен";
        try{
            employeeMapper.updateDepForEmpl(emplid,department_of_employee.getDep_id());
            employeeMapper.updateIsBoss(emplid,false);
        }
        catch (RuntimeException e){
            mess="Ошибка при переводе сотрудника.";
        }
        return mess;
    }

    //E7
    @PatchMapping("/transfer/all")
    public String transferEmployees(@RequestParam("to")int todep,@RequestBody Department_of_employee department_of_employee ){

        String mess="Все сотрудники успешно переведены";
        try{
            employeeMapper.updateIsBossForAll(department_of_employee.getDep_id());
            employeeMapper.updateDepForEmpls(department_of_employee.getDep_id(),todep);

        }
        catch (RuntimeException e){
            mess="Ошибка при переводе сотрудников.";
        }
        return mess;
    }

    //E3
    @PutMapping("/update")
    public String updateEmployee(@RequestParam("id") int id,@RequestBody Employee employee){

        String mess="Информация о сотруднике успешно обновлена";

        mess=employeeValidator.IsEmployeeValid(employee.getFirst_name(),employee.getLast_name(),employee.getPatronymic(),
                employee.getDate_of_birth(),employee.getHire_date(),employee.getEmail()
                ,employee.getSalary().toString(),employee.getPhone_number(),employee.getIs_boss(),id);
        if(mess.equals("OK")) {
            try {
                employeeMapper.updateEmpl(id, employee.getFirst_name(), employee.getLast_name(), employee.getPatronymic(),
                        employee.getEmail(), employee.getHire_date(), employee.getSalary(), employee.getIs_boss(),
                        employee.getJob_id(), employee.getPhone_number());
            } catch (RuntimeException e) {
                mess = "Ошибка при переводе сотрудников.";
            }
        }
        return mess;
    }


    @PatchMapping("/update/salary")
    public String updateEmplSal2(@RequestParam("id")int id,@RequestBody Employee employee){
        String mess="Зарплата обновлена";
        if(employeeValidator.checkSalary(employee.getSalary().toString(),id)){
            employeeMapper.updateSalary(id, employee.getSalary());
        }else{
            mess="Зарплата не обновлена";
        }
        return mess;
    }

    @PatchMapping("/update/hiredate")
    public String updateEmplHireDate(@RequestParam("id")int id,@RequestBody Employee employee){
        String mess="";
        if(dateValidator.IsFirstDateMore(employee.getHire_date(),employeeMapper.findDob(id))){
            employeeMapper.updateHireDate(id,employee.getHire_date());
            mess="Дата успешно обновлена";
        }else{
            mess="Не верно введена дата";
        }
        return mess;
    }

    @PatchMapping("/update/boss")
    public String updateBoss(@RequestParam("id")int id,@RequestBody Employee employee){
        String mess="Руководитель обновлен";
        try{
            if(employeeValidator.IsBossValid(employee.getIs_boss(),id)){
                employeeMapper.updateIsBoss(id,employee.getIs_boss());
            }else {
                mess="Уже существует руководитель.";
            }
        } catch (RuntimeException e){
            mess="Ошибка при обновление руководителя.";
        }
        return mess;
    }


    //Testing
    @GetMapping("/valid")
    public String check(){
        String mess="Not_null";
        try{
            int test=employeeMapper.findBossId(2);
            mess=mess+test;
        }
        catch (NullPointerException e){
            mess="Null";

        }
        return mess;
    }







}
