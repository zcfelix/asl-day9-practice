package com.afs.restapi;

import com.afs.restapi.entity.Employee;
import com.afs.restapi.repository.EmployeeRepository;
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

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.clearAll();
    }

    @Test
    void should_update_employee_age_and_salary() throws Exception {
        Employee previousEmployee = new Employee(1L, "zhangsan", 22, "Male", 1000);
        employeeRepository.insert(previousEmployee);

        Employee employeeUpdateRequest = new Employee(1L, "lisi", 24, "Female", 2000);
        ObjectMapper objectMapper = new ObjectMapper();
        String updatedEmployeeJson = objectMapper.writeValueAsString(employeeUpdateRequest);
        mockMvc.perform(put("/employees/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedEmployeeJson))
                .andExpect(MockMvcResultMatchers.status().is(204));

        Optional<Employee> optionalEmployee = employeeRepository.findById(1L);
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(employee.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(employee.getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value(employee.getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(employee.getSalary()));
    }

    @Test
    void should_find_employees() throws Exception {
        Employee employee = getEmployeeZhangsan();
        employeeRepository.insert(employee);

        mockMvc.perform(get("/employees"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(employee.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(employee.getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value(employee.getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(employee.getSalary()));
    }

    @Test
    void should_find_employee_by_id() throws Exception {
        Employee employee = getEmployeeZhangsan();
        employeeRepository.insert(employee);

        mockMvc.perform(get("/employees/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(employee.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(employee.getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value(employee.getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(employee.getSalary()));
    }

    @Test
    void should_delete_employee_by_id() throws Exception {
        Employee employee = getEmployeeZhangsan();
        employeeRepository.insert(employee);

        mockMvc.perform(delete("/employees/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().is(204));

        assertTrue(employeeRepository.findById(1L).isEmpty());
    }

    @Test
    void should_find_employee_by_gender() throws Exception {
        Employee employee = getEmployeeZhangsan();
        employeeRepository.insert(employee);

        mockMvc.perform(get("/employees?gender={0}", "Male"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
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
        employeeRepository.insert(employeeZhangsan);
        employeeRepository.insert(employeeSusan);
        employeeRepository.insert(employeeLisi);

        mockMvc.perform(get("/employees")
                        .param("page", "1")
                        .param("size", "2"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(employeeZhangsan.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(employeeZhangsan.getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value(employeeZhangsan.getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(employeeZhangsan.getSalary()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(employeeSusan.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].age").value(employeeSusan.getAge()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].gender").value(employeeSusan.getGender()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].salary").value(employeeSusan.getSalary()));
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