package com.depatment.department;

import com.depatment.department.models.Department;
import com.depatment.department.models.Employee;
import org.apache.ibatis.type.MappedTypes;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableScheduling
@EnableSwagger2
@MappedTypes({Department.class, Employee.class})
@MapperScan("com.depatment.department.mappers")
@SpringBootApplication
public class DepartmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(DepartmentApplication.class, args);
	}

	@Bean
    public Docket productApi(){
	    return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("com.depatment.department.controllers"))
                .build();
    }
}
