package com.depatment.department.validator;

import com.depatment.department.mappers.EmployeeMapper;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmployeeValidator {

    private final DateValidator dateValidator;

    private final EmployeeMapper employeeMapper;

    public EmployeeValidator(DateValidator dateValidator,EmployeeMapper employeeMapper){
        this.dateValidator=dateValidator;
        this.employeeMapper=employeeMapper;
    }

    //FLC1
    public boolean IsNameValid(String name){
      try {
          Pattern p=Pattern.compile("^[а-яА-Я_-]+");
          Matcher matcher=p.matcher(name);
          return matcher.matches();
      }catch (NullPointerException e){
          return true;
      }


    }
    //FLC3
    public boolean IsPhoneValid(String pnum){
        Pattern p=Pattern.compile("^[0-9_+-_(_)_ ]+");
        Matcher matcher=p.matcher(pnum);
        return matcher.matches();
    }
    //FlC4
    public boolean IsEmailValid(String email){
        return org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email);
    }

    //FlC7
    public boolean IsSalaryValid(String sal){
        Pattern p=Pattern.compile("^[0-9]+");
        Matcher matcher=p.matcher(sal);
        return matcher.matches();
    }

    public boolean checkSalary(String sal,int id){
        if(IsSalaryValid(sal)){
            int salary=Integer.parseInt(sal);
            try {
                if (employeeMapper.findBossId(id) != id) {
                    int bosssal = Integer.parseInt(employeeMapper.findBossSalary(id));
                    return salary < bosssal;
                } else {
                    return true;

                }
            }catch (NullPointerException e){
                return true;

            }
        }else{
           return false;
        }
    }

    public boolean checkSalary(int depid,String sal){
        if(IsSalaryValid(sal)){
            int salary=Integer.parseInt(sal);
            try {
                if (employeeMapper.findBossIdOfDepartment(depid) != depid) {
                    int bosssal = Integer.parseInt(employeeMapper.findBossSalaryOfDepartment(depid));
                    return salary < bosssal;
                } else {
                    return true;

                }
            }catch (NullPointerException e){
                return true;

            }
        }else{
            return false;
        }
    }

    public boolean IsBossValid(boolean isboss,int id){
        try {
            int bossid=employeeMapper.findBossId(id);
            if(isboss==false) return true;
            else return false;
        }catch (NullPointerException e){
            return true;
        }

    }
    public boolean IsBossValid(int depid,boolean isboss){
        try {
            int bossid=employeeMapper.findBossIdOfDepartment(depid);
            return isboss == false;
        }catch (NullPointerException e){
            return true;
        }

    }

    private String simpleCheck(String fname, String lname, String patr, String dob, String hdate, String email, String phone, String result, boolean b, boolean b2) {
        if(!IsNameValid(fname)){
            result="Firstname is not valid";
        }else if (!IsNameValid(lname)){
            result=result+"Lastname is not valid";
        }else if (!IsNameValid(patr)){
            result=result+"Patr is not valid";
        }else if(!dateValidator.IsDateValid(dob)){
            result=result+"Dob is not valid";
        }else if(!dateValidator.IsFirstDateMore(hdate,dob)){
            result=result+"HireDate is not valid";
        }else if(!IsEmailValid(email)){
            result=result+"Email is not valid";
        }else if(!IsPhoneValid(phone)){
            result=result+"Phone is not valid";
        }else if(!b){
            result=result+"Salary is not valid";
        }else if(!b2){
            result=result+"Boss is not valid";
        }else {
            result="OK";
        }
        return result;
    }


    public String IsEmployeeValid(int depid,String fname,String lname,String patr,String dob,String hdate,String email,String salary,String phone,boolean isboss){
        String result="";
        result = simpleCheck(fname, lname, patr, dob, hdate, email, phone, result, checkSalary(depid, salary), IsBossValid(depid, isboss));
        return result;
    }



    public String IsEmployeeValid(String fname,String lname,String patr,String dob,String hdate,String email,String salary,String phone,boolean isboss,int id){
        String result="";
        result = simpleCheck(fname, lname, patr, dob, hdate, email, phone, result, checkSalary(salary,id), IsBossValid(isboss,id));
        return result;
    }

}
