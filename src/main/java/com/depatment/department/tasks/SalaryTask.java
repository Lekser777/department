package com.depatment.department.tasks;

import com.depatment.department.mappers.DepartmentMapper;
import com.depatment.department.models.Department;
import com.depatment.department.models.SalaryFunds;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SalaryTask {

    private final DepartmentMapper departmentMapper;

    public SalaryTask(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    @Scheduled(fixedRate = 300000)
    public void reportSalaryFund() {

        try {
            List<Department> all = departmentMapper.findAll();
            List<SalaryFunds> allsf = departmentMapper.findAllSalFunds();
            SalaryFunds salaryFunds = new SalaryFunds();
            boolean isUpdated = false;
            if (allsf.size() == 0) {
                for (int i = 0; i < all.size(); i++) {
                    salaryFunds.setDep_id(all.get(i).getId());
                    departmentMapper.insertSalaryFund(salaryFunds);
                }
            } else {
                for (int it = 0; it < all.size(); it++) {
                    for (int ii = 0; ii < allsf.size(); ii++) {
                        if (all.get(it).getId().equals(allsf.get(ii).getDep_id())) {
                            departmentMapper.updateSalaryFunds(allsf.get(ii).getDep_id());
                            isUpdated = true;
                        }
                    }
                    if (!isUpdated) {
                        salaryFunds.setDep_id(all.get(it).getId());
                        departmentMapper.insertSalaryFund(salaryFunds);
                    }
                    isUpdated = false;

                }

            }
        }catch (RuntimeException e){
        }
    }
}

