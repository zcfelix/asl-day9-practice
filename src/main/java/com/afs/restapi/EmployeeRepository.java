package com.afs.restapi;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepository {

    private List<Employee> employees = new ArrayList<>();

    public List<Employee> getEmployees() {
        return employees;
    }

    public Employee findById(Long id) {
        return getEmployees().stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Employee> findAllByGender(String gender) {
        return getEmployees().stream()
                .filter(employee -> employee.getGender().equals(gender))
                .collect(Collectors.toList());
    }

    public Employee insert(Employee employee) {
        employee.setId(nextId());
        getEmployees().add(employee);
        return employee;
    }

    public void clearAll() {
        employees.clear();
    }

    private Long nextId() {
        long maxId = getEmployees().stream()
                .mapToLong(Employee::getId)
                .max()
                .orElse(0L);
        return maxId + 1;
    }

}
