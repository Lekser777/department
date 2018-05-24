package com.depatment.department.validator;

import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateValidator {

    //FlC2
    public boolean IsDateValid(String date){
        return GenericValidator.isDate(date,"yyyy-MM-dd",true);
    }

    //FlC5
    public boolean IsFirstDateMore(String first,String second){
        DateFormat format =new SimpleDateFormat("yyyy-MM-dd");
        try{Date firstdate=format.parse(first);
            Date seconddated=format.parse(second);
            if(firstdate.getTime()>seconddated.getTime()){
                return true;
            }else {
                return false;
            }
        }catch (ParseException e){
            System.out.println("ParseExeption while checking hdate validation");
            return false;
        }
    }


}
