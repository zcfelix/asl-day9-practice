package com.afs.restapi.repository;

import com.afs.restapi.entity.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InMemoryEmployeeRepository {

    private List<Employee> employees = new ArrayList<>();

    public List<Employee> getEmployees() {
        return employees;
    }

    public Optional<Employee> findById(Long id) {
        return getEmployees().stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst();
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

    public List<Employee> findByCompanyId(Long companyId) {
        return employees.stream()
                .filter(employee -> employee.getCompanyId().equals(companyId))
                .collect(Collectors.toList());
    }

    public List<Employee> findByPage(Integer page, Integer size) {
        return employees.stream()
                .skip((long) (page - 1) * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        findById(id).ifPresent(employee -> employees.remove(employee));
    }
}
