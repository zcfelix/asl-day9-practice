package com.afs.restapi.service;

import com.afs.restapi.entity.Employee;
import com.afs.restapi.exception.EmployeeNotFoundException;
import com.afs.restapi.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeRepository getEmployeeRepository() {
        return employeeRepository;
    }

    public List<Employee> findAll() {
        return getEmployeeRepository().getEmployees();
    }

    public Employee findById(Long id) {
        return getEmployeeRepository().findById(id)
                .orElseThrow(EmployeeNotFoundException::new);
    }

    public void update(Long id, Employee employee) {
        Employee toBeUpdatedEmployee = findById(id);
        if (employee.getSalary() != null) {
            toBeUpdatedEmployee.setSalary(employee.getSalary());
        }
        if (employee.getAge() != null) {
            toBeUpdatedEmployee.setAge(employee.getAge());
        }
    }

    public List<Employee> findAllByGender(String gender) {
        return getEmployeeRepository().findAllByGender(gender);
    }

    public Employee create(Employee employee) {
        return getEmployeeRepository().insert(employee);
    }

    public List<Employee> findByPage(Integer page, Integer size) {
        return getEmployeeRepository().findByPage(page, size);
    }

    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }
}
