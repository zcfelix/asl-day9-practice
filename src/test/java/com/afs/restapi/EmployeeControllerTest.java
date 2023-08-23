package com.afs.restapi;

import com.afs.restapi.entity.Employee;
import com.afs.restapi.repository.EmployeeJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeJpaRepository employeeJpaRepository;

    @BeforeEach
    void setUp() {
        employeeJpaRepository.deleteAll();
    }

    @Test
    void should_update_employee_age_and_salary() throws Exception {
        Employee previousEmployee = new Employee(null, "zhangsan", 22, "Male", 1000);
        Employee savedEmployee = employeeJpaRepository.save(previousEmployee);

        Employee employeeUpdateRequest = new Employee(1L, "lisi", 24, "Female", 2000);
        ObjectMapper objectMapper = new ObjectMapper();
        String updatedEmployeeJson = objectMapper.writeValueAsString(employeeUpdateRequest);
        mockMvc.perform(put("/employees/{id}", savedEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEmployeeJson))
                .andExpect(MockMvcResultMatchers.status().is(204));

        Optional<Employee> optionalEmployee = employeeJpaRepository.findById(savedEmployee.getId());
        assertTrue(optionalEmployee.isPresent());
        Employee updatedEmployee = optionalEmployee.get();
        Assertions.assertEquals(employeeUpdateRequest.getAge(), updatedEmployee.getAge());
        Assertions.assertEquals(employeeUpdateRequest.getSalary(), updatedEmployee.getSalary());
        Assertions.assertEquals(previousEmployee.getId(), updatedEmployee.getId());
        Assertions.assertEquals(previousEmployee.getName(), updatedEmployee.getName());
        Assertions.assertEquals(previousEmployee.getGender(), updatedEmployee.getGender());
    }

    @Test
    void should_create_employee() throws Exception {
        Employee employee = getEmployeeZhangsan();

        ObjectMapper objectMapper = new ObjectMapper();
        String employeeRequest = objectMapper.writeValueAsString(employee);
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeRequest))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(employee.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(employee.getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value(employee.getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(employee.getSalary()));
    }

    @Test
    void should_find_employees() throws Exception {
        Employee employee = getEmployeeZhangsan();
        Employee savedEmployee = employeeJpaRepository.save(employee);

        mockMvc.perform(get("/employees"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(savedEmployee.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(savedEmployee.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(savedEmployee.getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value(savedEmployee.getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(savedEmployee.getSalary()));
    }

    @Test
    void should_find_employee_by_id() throws Exception {
        Employee employee = getEmployeeZhangsan();
        Employee savedEmployee = employeeJpaRepository.save(employee);

        mockMvc.perform(get("/employees/{id}", savedEmployee.getId()))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedEmployee.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(savedEmployee.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(savedEmployee.getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value(savedEmployee.getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(savedEmployee.getSalary()));
    }

    @Test
    void should_delete_employee_by_id() throws Exception {
        Employee employee = getEmployeeZhangsan();
        Employee savedEmployee = employeeJpaRepository.save(employee);

        mockMvc.perform(delete("/employees/{id}", savedEmployee.getId()))
                .andExpect(MockMvcResultMatchers.status().is(204));

        assertTrue(employeeJpaRepository.findById(1L).isEmpty());
    }

    @Test
    void should_find_employee_by_gender() throws Exception {
        Employee employee = getEmployeeZhangsan();
        Employee savedEmployee = employeeJpaRepository.save(employee);

        mockMvc.perform(get("/employees?gender={0}", "Male"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(savedEmployee.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(employee.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(employee.getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value(employee.getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(employee.getSalary()));
    }

    @Test
    void should_find_employees_by_page() throws Exception {
        Employee employeeZhangsan = getEmployeeZhangsan();
        Employee employeeSusan = getEmployeeSusan();
        Employee employeeLisi = getEmployeeLisi();
        Employee savedZhangsan = employeeJpaRepository.save(employeeZhangsan);
        Employee savedSusan = employeeJpaRepository.save(employeeSusan);
        Employee savedLisi = employeeJpaRepository.save(employeeLisi);

        mockMvc.perform(get("/employees")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(savedZhangsan.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(savedZhangsan.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(savedZhangsan.getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value(savedZhangsan.getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(savedZhangsan.getSalary()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(savedSusan.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(savedSusan.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age").value(savedSusan.getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].gender").value(savedSusan.getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].salary").value(savedSusan.getSalary()));
    }

    private static Employee getEmployeeZhangsan() {
        Employee employee = new Employee();
        employee.setName("zhangsan");
        employee.setAge(22);
        employee.setGender("Male");
        employee.setSalary(10000);
        return employee;
    }

    private static Employee getEmployeeSusan() {
        Employee employee = new Employee();
        employee.setName("susan");
        employee.setAge(23);
        employee.setGender("Male");
        employee.setSalary(11000);
        return employee;
    }

    private static Employee getEmployeeLisi() {
        Employee employee = new Employee();
        employee.setName("lisi");
        employee.setAge(24);
        employee.setGender("Female");
        employee.setSalary(12000);
        return employee;
    }
}