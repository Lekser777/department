package com.depatment.department.controllers;

import com.depatment.department.mappers.EmployeeMapper;
import com.depatment.department.models.Department_of_employee;
import com.depatment.department.models.Employee;
import com.depatment.department.validator.DateValidator;
import com.depatment.department.validator.EmployeeValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/employee")
@Api(value = "employee",description = "Действия производимые с таблицей \"Employee\"")
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
    @ApiOperation(value = "Получение списка сотрудников департамента")
    @GetMapping("/find/ofdepartment")
    public List<Employee> getEmplOfDep(@RequestParam("depid") int depid){
        return employeeMapper.findEmpOfDepartment(depid);
    }

    //E5
    @ApiOperation(value = "Получение информации о сотруднике по Id")
    @GetMapping("/find/byid")
    public Employee getEmplById(@RequestParam("id") int id){
        return employeeMapper.findEmpById(id);
    }

    //E8
    @ApiOperation(value = "Получение информации о руководителе данного сотрудника")
    @GetMapping("/find/boss")
    public Employee getBossOfEmpl(@RequestParam("id") int id){
        return employeeMapper.findBossOfEmpl(id);
    }

    //E9
    @ApiOperation(value = "Поиск сотрудников с зарплатой больше чем введенный параметр")
    @GetMapping("/find/salarymorethan")
    public List<Employee> getEmplWithSalaryMoreThan(@RequestParam("salary") int salary){
        return employeeMapper.findEmplWithSalaryMoreThan(salary);
    }

    @ApiOperation(value = "Поиск даты принятия на работу по Id")
    @GetMapping("/find/hdateof")
    public String findEmplHDate(@RequestParam("id") int id){
        return employeeMapper.findHireDate(id);
    }

    @ApiOperation(value = "Поиск дня рождения по Id")
    @GetMapping("/find/dobof")
    public String findEmplDob(@RequestParam("id") int id){
        return employeeMapper.findDob(id);
    }

    //E2
    @ApiOperation(value = "Добавление сотрудника с указанием в каком дапертаменте он будет работать")
    @PostMapping("/add")
    public ResponseEntity addEmployee(@RequestBody Employee employee,@RequestParam("depid") int depid){

        String mess=employeeValidator.IsEmployeeValid(depid,employee.getFirst_name(),employee.getLast_name(),employee.getPatronymic(),
                employee.getDate_of_birth(),employee.getHire_date(),employee.getEmail()
                ,employee.getSalary().toString(),employee.getPhone_number(),employee.getIs_boss());
        if(mess.equals("OK")) {
            Department_of_employee doe = new Department_of_employee();
            doe.setDep_id(depid);
            doe.setEmpl_id(employee.getId());
            try {
                employeeMapper.insertEmployee(employee);
                employeeMapper.insertDepOfEmpl(doe);
            } catch (RuntimeException e) {
                mess=mess+" PSQLException";
            }
        }
        return new ResponseEntity(mess, HttpStatus.OK);
    }

    //E4
    @ApiOperation(value = "Увольненик сотрудника")
    @PatchMapping("/retire")
    public ResponseEntity retireEmployee(@RequestParam("id") int id, @RequestBody Employee employee){

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
        return new ResponseEntity(mess, HttpStatus.OK);
    }

    //E6
    @ApiOperation(value = "Перевод сотрудника в другой департамент")
    @PatchMapping("/transfer")
    public ResponseEntity transferEmployee(@RequestParam("emplid") int emplid,@RequestBody Department_of_employee department_of_employee){

        String mess="Сотрудник успешно переведен";
        try{
            employeeMapper.updateDepForEmpl(emplid,department_of_employee.getDep_id());
            employeeMapper.updateIsBoss(emplid,false);
        }
        catch (RuntimeException e){
            mess="Ошибка при переводе сотрудника.";
        }
        return new ResponseEntity(mess, HttpStatus.OK);
    }

    //E7
    @ApiOperation(value = "Перевод всх сотрудников из одного департамента в указанных")
    @PatchMapping("/transfer/all")
    public ResponseEntity transferEmployees(@RequestParam("to")int todep,@RequestBody Department_of_employee department_of_employee ){

        String mess="Все сотрудники успешно переведены";
        try{
            employeeMapper.updateIsBossForAll(department_of_employee.getDep_id());
            employeeMapper.updateDepForEmpls(department_of_employee.getDep_id(),todep);

        }
        catch (RuntimeException e){
            mess="Ошибка при переводе сотрудников.";
        }
        return new ResponseEntity(mess, HttpStatus.OK);
    }

    //E3
    @ApiOperation(value = "Редактирование сведений о сотруднике департамента")
    @PutMapping("/update")
    public ResponseEntity updateEmployee(@RequestParam("id") int id,@RequestBody Employee employee){

        String mess=employeeValidator.IsEmployeeValid(employee.getFirst_name(),employee.getLast_name(),employee.getPatronymic(),
                employee.getDate_of_birth(),employee.getHire_date(),employee.getEmail(),
                employee.getSalary().toString(),employee.getPhone_number(),employee.getIs_boss(),id);
        if(mess.equals("OK")) {
            try {
                employeeMapper.updateEmpl(id, employee.getFirst_name(), employee.getLast_name(), employee.getPatronymic(),
                        employee.getEmail(), employee.getHire_date(), employee.getSalary(), employee.getIs_boss(),
                        employee.getJob_id(), employee.getPhone_number());
            } catch (RuntimeException e) {
                mess = "Ошибка при переводе сотрудников.";
            }
        }
        return new ResponseEntity(mess, HttpStatus.OK);
    }

    @ApiOperation(value = "Изменение зарплаты сотрудника")
    @PatchMapping("/update/salary")
    public ResponseEntity updateEmplSal2(@RequestParam("id")int id,@RequestBody Employee employee){
        String mess="Зарплата обновлена";
        if(employeeValidator.checkSalary(employee.getSalary().toString(),id)){
            employeeMapper.updateSalary(id, employee.getSalary());
        }else{
            mess="Зарплата не обновлена";
        }
        return new ResponseEntity(mess, HttpStatus.OK);
    }

    @ApiOperation(value = "Изменение даты принятия на работу сотрудника")
    @PatchMapping("/update/hiredate")
    public ResponseEntity updateEmplHireDate(@RequestParam("id")int id,@RequestBody Employee employee){
        String mess="";
        if(dateValidator.IsFirstDateMore(employee.getHire_date(),employeeMapper.findDob(id))){
            employeeMapper.updateHireDate(id,employee.getHire_date());
            mess="Дата успешно обновлена";
        }else{
            mess="Не верно введена дата";
        }
        return new ResponseEntity(mess, HttpStatus.OK);
    }

    @ApiOperation(value = "Повышение сотрудника до руководителя")
    @PatchMapping("/update/boss")
    public ResponseEntity updateBoss(@RequestParam("id")int id,@RequestBody Employee employee){
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
        return new ResponseEntity(mess, HttpStatus.OK);
    }









}
