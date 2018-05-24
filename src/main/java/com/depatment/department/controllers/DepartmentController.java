package com.depatment.department.controllers;

import com.depatment.department.mappers.DepartmentMapper;
import com.depatment.department.models.Department;
import com.depatment.department.models.DepartmentWithInfo;
import com.depatment.department.models.Departments_dependence;
import com.depatment.department.models.SalaryFund;
import com.depatment.department.validator.DateValidator;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentMapper departmentMapper;
    private final DateValidator dateValidator;

    public DepartmentController(DepartmentMapper departmentMapper, DateValidator dateValidator){
        this.departmentMapper=departmentMapper;
        this.dateValidator=dateValidator;
    }

//GET
    @GetMapping("/find/all")
    public List<Department> getAll(){
        return departmentMapper.findAll();
    }

    @GetMapping("/find/byid/{id}")
    public DepartmentWithInfo getById(@PathVariable int id){
        return departmentMapper.findById(id);
    }

    @GetMapping("/find/lower/byonelvl/{id}")
    public List<Department> getAllLowerByLvl(@PathVariable int id){
        return departmentMapper.findAllLowerByLvl(id);
    }

    @GetMapping("/find/lower/byalllvl/{id}")
    public List<Department> getAllLowerByAllLvl(@PathVariable int id){
        return departmentMapper.findAllLowerByAllLvl(id);
    }
    @GetMapping("/find/higher/byalllvl/{id}")
    public List<Department> getAllHigherByAllLvl(@PathVariable int id){
        return departmentMapper.findAllHigherByAllLvl(id);
    }
    @GetMapping("/find/byname/{name}")
    public Department getByName(@PathVariable String name){
        return departmentMapper.findByName(name);
    }

    @GetMapping("/find/salary/{id}")
    public SalaryFund getSalaryFundById(@PathVariable int id){
        return departmentMapper.findFundByid(id);
    }


    @Transactional
    @PostMapping("/add/")
    public String addDepartment(@RequestBody Department department, @RequestParam(value = "toid",defaultValue = "0") int toid) {
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
        //date можно проверить в кэче#Validation
        return mess;
    }


    @PatchMapping("/update")
    public String updateDepName(@RequestBody Department department, @RequestParam("curname") String curname){
        String mess="Успешное обновление названия департамената";
        try{departmentMapper.updateDepName(curname,department.getName());}//обработка исключения не верно работает
        catch (RuntimeException e){
            mess="Ошибка при обновление. Выберете другое имя департамента";

        }
        return mess;
    }

    @PatchMapping("/update/dependency")
    public String updateDependency(@RequestBody Departments_dependence departments_dependence,@RequestParam("depid") int depid){
        String mess="Успешное обновление";
        try{departmentMapper.updateDependency(depid,departments_dependence.getLinked_to_dep_id());}
        catch (RuntimeException e){
            mess="Ошибка при обновление зависимостей";
        }
        return mess;
    }


    @DeleteMapping("/delete")//need to check
    public String deleteDep(@RequestParam("name") String name){
        String mess="Успешное удаление";
        try{departmentMapper.deleteDep(name);}
        catch (RuntimeException e){
            mess="Ошибка при удалении. Удаление возможно, только если в департаменте нет ни одного сотрудника";
        }
        return mess;
    }




}
