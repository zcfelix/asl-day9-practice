package com.afs.restapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @GetMapping
    public List<Employee> getAllEmployees() {
        return List.of(new Employee(1L, "zhangsan", 20, "Male", 1000));
    }
}
