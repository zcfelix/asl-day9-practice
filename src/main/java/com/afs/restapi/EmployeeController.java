package com.afs.restapi;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController() {
        this.employeeRepository = new EmployeeRepository();
    }

    public List<Employee> getEmployees() {
        return employeeRepository.getEmployees();
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.getEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeRepository.findById(id);
    }

    @GetMapping(params = "gender")
    public List<Employee> getEmployeesByGender(@RequestParam String gender) {
        return employeeRepository.findAllByGender(gender);
    }

    @PostMapping
    public void createEmployee(@RequestBody Employee employee) {
        employeeRepository.insert(employee);
    }

}
