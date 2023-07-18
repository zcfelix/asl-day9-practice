package com.afs.restapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    public static final List<Employee> EMPLOYEES = List.of(new Employee(1L, "zhangsan", 20, "Male", 1000));

    @GetMapping
    public List<Employee> getAllEmployees() {
        return EMPLOYEES;
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return EMPLOYEES.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
