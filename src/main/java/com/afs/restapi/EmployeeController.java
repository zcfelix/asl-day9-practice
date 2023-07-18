package com.afs.restapi;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    public final List<Employee> employees = new ArrayList<>();
    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return getEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return getEmployees().stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @GetMapping(params = "gender")
    public List<Employee> getEmployeesByGender(@RequestParam String gender) {
        return getEmployees().stream()
                .filter(employee -> employee.getGender().equals(gender))
                .collect(Collectors.toList());
    }

    @PostMapping
    public void createEmployee(@RequestBody Employee employee) {
        employee.setId(nextId());
        getEmployees().add(employee);
    }

    private Long nextId() {
        long maxId = getEmployees().stream()
                .mapToLong(Employee::getId)
                .max()
                .orElse(0L);
        return maxId + 1;
    }

}
