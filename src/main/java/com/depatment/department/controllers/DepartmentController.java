package com.depatment.department.controllers;

import com.depatment.department.mappers.DepartmentMapper;
import com.depatment.department.models.Department;
import com.depatment.department.models.DepartmentWithInfo;
import com.depatment.department.models.Departments_dependence;
import com.depatment.department.models.SalaryFund;
import com.depatment.department.validator.DateValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/department")
@Api(value = "department",description = "Действия производимые с таблицей \"Department\"")
public class DepartmentController {

    private final DepartmentMapper departmentMapper;
    private final DateValidator dateValidator;

    public DepartmentController(DepartmentMapper departmentMapper, DateValidator dateValidator){
        this.departmentMapper=departmentMapper;
        this.dateValidator=dateValidator;
    }

//GET
    @ApiOperation(value = "Поиск всех департаментов")
    @GetMapping("/find/all")
    public List<Department> getAll(){
        return departmentMapper.findAll();
    }

    @ApiOperation(value = "Поиск департамента по Id")
    @GetMapping("/find/byid/{id}")
    public DepartmentWithInfo getById(@PathVariable int id){
        return departmentMapper.findById(id);
    }

    @ApiOperation(value = "Предоставление информации о департаментах, находящихся в непосредственном подчинении данного департамента (на уровень ниже)")
    @GetMapping("/find/lower/byonelvl/{id}")
    public List<Department> getAllLowerByLvl(@PathVariable int id){
        return departmentMapper.findAllLowerByLvl(id);
    }

    @ApiOperation(value = "Предоставление информации о всех департаментах, находящихся в подчинении данного департамента (все подчиненные департаменты)")
    @GetMapping("/find/lower/byalllvl/{id}")
    public List<Department> getAllLowerByAllLvl(@PathVariable int id){
        return departmentMapper.findAllLowerByAllLvl(id);
    }

    @ApiOperation(value = "Получение информации о всех вышестоящих департаментах данного департамента")
    @GetMapping("/find/higher/byalllvl/{id}")
    public List<Department> getAllHigherByAllLvl(@PathVariable int id){
        return departmentMapper.findAllHigherByAllLvl(id);
    }

    @ApiOperation(value = "Поиск департамента по наименованию")
    @GetMapping("/find/byname/{name}")
    public Department getByName(@PathVariable String name){
        return departmentMapper.findByName(name);
    }

    @ApiOperation(value = "Получение информации о фонде заработной платы департамента (сумма зарплат всех сотрудников департамента)")
    @GetMapping("/find/salary/{id}")
    public SalaryFund getSalaryFundById(@PathVariable int id){
        return departmentMapper.findFundByid(id);
    }

    @ApiOperation(value = "Создание департамента")
    @Transactional
    @PostMapping("/add/")
    public ResponseEntity addDepartment(@RequestBody Department department, @RequestParam(value = "toid",defaultValue = "0") int toid) {
        String mess = "Успешное добавление";
        if (dateValidator.IsDateValid(department.getCreation_date())) {
            Departments_dependence dd = new Departments_dependence();
            dd.setMain_dep_id(toid);
            dd.setLinked_to_dep_id(department.getId());
            try {
                if (!departmentMapper.findAll().equals(null)) {
                    departmentMapper.insertDepartment(department);
                    departmentMapper.insertDepartment_dep(dd);
                } else {
                    departmentMapper.insertDepartment(department);
                }
            } catch (RuntimeException e) {
                mess = "Ошибка при добавление.";
            }
        }else mess="Дата введена не коректно";
        return new ResponseEntity(mess, HttpStatus.OK);
    }

    @ApiOperation(value = "Обновление имени департамента")
    @PatchMapping("/update")
    public ResponseEntity updateDepName(@RequestBody Department department, @RequestParam("curname") String curname){
        String mess="Успешное обновление названия департамената";
        try{departmentMapper.updateDepName(curname,department.getName());}
        catch (RuntimeException e){
            mess="Ошибка при обновление. Выберете другое имя департамента";

        }
        return new ResponseEntity(mess, HttpStatus.OK);
    }

    @ApiOperation(value = "Перенос департамента. Задание другого департамента, куда будет входить данный департамент")
    @PatchMapping("/update/dependency")
    public ResponseEntity updateDependency(@RequestBody Departments_dependence departments_dependence,@RequestParam("id") int id){
        String mess="Успешное обновление";
        try{departmentMapper.updateDependency(id,departments_dependence.getMain_dep_id());}
        catch (RuntimeException e){
            mess="Ошибка при обновление зависимостей";
        }
        return new ResponseEntity(mess, HttpStatus.OK);
    }

    @ApiOperation(value = "Удаление департамента")
    @DeleteMapping("/delete")
    public ResponseEntity deleteDep(@RequestParam("name") String name){
        String mess="Успешное удаление";
        try{departmentMapper.deleteDep(name);}
        catch (RuntimeException e){
            mess="Ошибка при удалении. Удаление возможно, только если в департаменте нет ни одного сотрудника";
        }
        return new ResponseEntity(mess, HttpStatus.OK);
    }




}
