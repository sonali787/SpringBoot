package com.example.testing.controllers;

import com.example.testing.dto.EmployeeDto;
import com.example.testing.entities.Employee;
import com.example.testing.repositories.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Clean H2 database state before each test execution
        employeeRepository.deleteAll();
    }

    // ==========================================
    // 1. POST /api/employees (Integration)
    // ==========================================

    @Test
    @DisplayName("Integration Test for createEmployee REST API")
    void givenEmployeeObject_whenCreateEmployee_thenSaveAndReturnCreatedEmployee() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto(
                null, "John", "Doe", "john.doe@example.com", "IT", new BigDecimal("75000.00")
        );

        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));

        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(1);
    }

    // ==========================================
    // 2. GET /api/employees/{id} (Integration)
    // ==========================================

    @Test
    @DisplayName("Integration Test for getEmployeeById REST API - Success")
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        Employee savedEmployee = employeeRepository.save(
                new Employee(null, "Jane", "Smith", "jane.smith@example.com", "HR", new BigDecimal("80000.00"))
        );

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", savedEmployee.getId()));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Jane")))
                .andExpect(jsonPath("$.email", is("jane.smith@example.com")));
    }

    @Test
    @DisplayName("Integration Test for getEmployeeById REST API - 404 Not Found")
    void givenInvalidEmployeeId_whenGetEmployeeById_thenReturn404NotFound() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", 99L));

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is("RESOURCE_NOT_FOUND")));
    }

    // ==========================================
    // 3. GET /api/employees/email/{email} (Integration)
    // ==========================================

    @Test
    @DisplayName("Integration Test for getEmployeeByEmail REST API")
    void givenEmployeeEmail_whenGetEmployeeByEmail_thenReturnEmployeeObject() throws Exception {
        employeeRepository.save(
                new Employee(null, "Mark", "Twain", "mark.twain@example.com", "Sales", new BigDecimal("65000.00"))
        );

        ResultActions response = mockMvc.perform(get("/api/employees/email/{email}", "mark.twain@example.com"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Mark")))
                .andExpect(jsonPath("$.email", is("mark.twain@example.com")));
    }

    // ==========================================
    // 4. GET /api/employees (Integration)
    // ==========================================

    @Test
    @DisplayName("Integration Test for getAllEmployees REST API")
    void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {
        employeeRepository.saveAll(List.of(
                new Employee(null, "John", "Doe", "john@example.com", "IT", new BigDecimal("75000.00")),
                new Employee(null, "Jane", "Smith", "jane@example.com", "HR", new BigDecimal("80000.00"))
        ));

        ResultActions response = mockMvc.perform(get("/api/employees"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    // ==========================================
    // 5. PUT /api/employees/{id} (Integration)
    // ==========================================

    @Test
    @DisplayName("Integration Test for updateEmployee REST API")
    void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        Employee savedEmployee = employeeRepository.save(
                new Employee(null, "John", "Doe", "john.doe@example.com", "IT", new BigDecimal("75000.00"))
        );

        EmployeeDto updatedDto = new EmployeeDto(
                null, "JohnUpdated", "DoeUpdated", "john.updated@example.com", "Engineering", new BigDecimal("95000.00")
        );

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("JohnUpdated")))
                .andExpect(jsonPath("$.department", is("Engineering")));

        Employee updatedInDb = employeeRepository.findById(savedEmployee.getId()).orElseThrow();
        assertThat(updatedInDb.getFirstName()).isEqualTo("JohnUpdated");
    }

    // ==========================================
    // 6. DELETE /api/employees/{id} (Integration)
    // ==========================================

    @Test
    @DisplayName("Integration Test for deleteEmployee REST API")
    void givenEmployeeId_whenDeleteEmployee_thenEmployeeIsDeletedFromDatabase() throws Exception {
        Employee savedEmployee = employeeRepository.save(
                new Employee(null, "John", "Doe", "john.doe@example.com", "IT", new BigDecimal("75000.00"))
        );

        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

        response.andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully."));

        assertThat(employeeRepository.findById(savedEmployee.getId())).isEmpty();
    }
}
