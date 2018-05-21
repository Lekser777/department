package com.depatment.department.controllers;

import com.depatment.department.mappers.DepartmentMapper;
import com.depatment.department.models.Department;
import com.depatment.department.models.DepartmentWithInfo;
import com.depatment.department.models.Departments_dependence;
import com.depatment.department.models.SalaryFund;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.sql.SQLException;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentMapper departmentMapper;
    public DepartmentController(DepartmentMapper departmentMapper){
        this.departmentMapper=departmentMapper;
    }

    @GetMapping("/all")
    public List<Department> getAll(){
        return departmentMapper.findAll();
    }
    @GetMapping("/byid/{id}")
    public DepartmentWithInfo getById(@PathVariable int id){
        return departmentMapper.findById(id);
    }
    @GetMapping("/lower/byonelvl/{id}")
    public List<Department> getAllLowerByLvl(@PathVariable int id){
        return departmentMapper.findAllLowerByLvl(id);
    }
    @GetMapping("/lower/byalllvl/{id}")
    public List<Department> getAllLowerByAllLvl(@PathVariable int id){
        return departmentMapper.findAllLowerByAllLvl(id);
    }
    @GetMapping("/higher/byalllvl/{id}")
    public List<Department> getAllHigherByAllLvl(@PathVariable int id){
        return departmentMapper.findAllHigherByAllLvl(id);
    }
    @GetMapping("/byname/{name}")
    public Department getByName(@PathVariable String name){
        return departmentMapper.findByName(name);
    }
    @GetMapping("/salary/{id}")
    public SalaryFund getSalaryFundById(@PathVariable int id){
        return departmentMapper.findFundByid(id);
    }

    @Transactional
    @GetMapping("/add/")
    public String addDepartment(@RequestParam("name") String name, @RequestParam("date")  @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestParam(value = "toid",defaultValue = "0") int toid){
        Department dep=new Department();
        dep.setName(name);
        dep.setCreation_date(date);

        Departments_dependence dd=new Departments_dependence();
        dd.setMain_dep_id(toid);
        dd.setLinked_to_dep_id(dep.getId());

        String mess="Успешное добавление";

        try {
            if(departmentMapper.findAll().equals(null)){
            departmentMapper.insertDepartment(dep);
            }else {
                departmentMapper.insertDepartment(dep);
                departmentMapper.insertDepartment_dep(dd);
            }
        }catch(SqlSessionException e) {
            mess = "Ошибка при добавление.";
        }

        //date можно проверить в кэче#Validation
        return mess;
    }

    @GetMapping("/update")
    public String updateDepName(@RequestParam("curname") String curname,@RequestParam("newname") String newname){
        String mess="Успешное обновление названия департамената";
        try{departmentMapper.updateDepName(curname,newname);}
        catch (SqlSessionException e){
            mess="Ошибка при обновление. Выберете другое имя департамента";

        }

        return mess;
    }

    @GetMapping("/delete")
    public String deleteDep(@RequestParam("name") String name){
        String mess="Успешное удаление";
        try{departmentMapper.deleteDep(name);}
        catch (SqlSessionException e){
            mess="Ошибка при удалении. Удаление возможно, только если в департаменте нет ни одного сотрудника";
        }
        return mess;
    }

    @GetMapping("/update/dependency")
    public String updateDependency(@RequestParam("depid") int depid,@RequestParam("linkedtoid") int linkedto){
        String mess="Успешное обновление";
        try{departmentMapper.updateDependency(depid,linkedto);}
        catch (SqlSessionException e){
            mess="Ошибка при обновление зависимостей";
        }
        return mess;
    }






}
