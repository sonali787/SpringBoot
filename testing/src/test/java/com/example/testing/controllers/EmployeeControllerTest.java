package com.example.testing.controllers;

import com.example.testing.dto.EmployeeDto;
import com.example.testing.exceptions.ResourceNotFoundException;
import com.example.testing.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeDto getSampleEmployeeDto() {
        return new EmployeeDto(
                1L, "John", "Doe", "john.doe@example.com", "IT", new BigDecimal("75000.00")
        );
    }

    // ==========================================
    // 1. CREATE EMPLOYEE (POST /api/employees)
    // ==========================================

    @Test
    @DisplayName("POST /api/employees - Happy Scenario (201 Created)")
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        EmployeeDto employeeDto = getSampleEmployeeDto();
        given(employeeService.createEmployee(any(EmployeeDto.class))).willReturn(employeeDto);

        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDto)));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                .andExpect(jsonPath("$.department", is("IT")))
                .andExpect(jsonPath("$.salary", is(75000.00)));
    }

    @Test
    @DisplayName("POST /api/employees - Sad Scenario (400 Bad Request on Validation Failure)")
    void givenInvalidEmployeeObject_whenCreateEmployee_thenReturn400BadRequest() throws Exception {
        // Blank email & empty first name (violates @Valid annotations)
        EmployeeDto invalidDto = new EmployeeDto(
                null, "", "Doe", "", "IT", new BigDecimal("75000.00")
        );

        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)));

        response.andExpect(status().isBadRequest());
    }

    // ==========================================
    // 2. GET EMPLOYEE BY ID (GET /api/employees/{id})
    // ==========================================

    @Test
    @DisplayName("GET /api/employees/{id} - Happy Scenario (200 OK)")
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        EmployeeDto employeeDto = getSampleEmployeeDto();
        given(employeeService.getEmployeeById(1L)).willReturn(employeeDto);

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", 1L));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    @DisplayName("GET /api/employees/{id} - Sad Scenario (404 Not Found)")
    void givenInvalidEmployeeId_whenGetEmployeeById_thenReturn404NotFound() throws Exception {
        given(employeeService.getEmployeeById(99L))
                .willThrow(new ResourceNotFoundException("Employee", "id", 99L));

        ResultActions response = mockMvc.perform(get("/api/employees/{id}", 99L));

        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode", is("RESOURCE_NOT_FOUND")));
    }

    // ==========================================
    // 3. GET EMPLOYEE BY EMAIL (GET /api/employees/email/{email})
    // ==========================================

    @Test
    @DisplayName("GET /api/employees/email/{email} - Happy Scenario (200 OK)")
    void givenEmployeeEmail_whenGetEmployeeByEmail_thenReturnEmployeeObject() throws Exception {
        EmployeeDto employeeDto = getSampleEmployeeDto();
        given(employeeService.getEmployeeByEmail("john.doe@example.com")).willReturn(employeeDto);

        ResultActions response = mockMvc.perform(get("/api/employees/email/{email}", "john.doe@example.com"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                .andExpect(jsonPath("$.firstName", is("John")));
    }

    @Test
    @DisplayName("GET /api/employees/email/{email} - Sad Scenario (404 Not Found)")
    void givenInvalidEmail_whenGetEmployeeByEmail_thenReturn404NotFound() throws Exception {
        given(employeeService.getEmployeeByEmail("notfound@example.com"))
                .willThrow(new ResourceNotFoundException("Employee", "email", "notfound@example.com"));

        ResultActions response = mockMvc.perform(get("/api/employees/email/{email}", "notfound@example.com"));

        response.andExpect(status().isNotFound());
    }

    // ==========================================
    // 4. GET ALL EMPLOYEES (GET /api/employees)
    // ==========================================

    @Test
    @DisplayName("GET /api/employees - Happy Scenario (200 OK with List)")
    void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {
        EmployeeDto employee1 = getSampleEmployeeDto();
        EmployeeDto employee2 = new EmployeeDto(
                2L, "Jane", "Smith", "jane.smith@example.com", "HR", new BigDecimal("80000.00")
        );
        given(employeeService.getAllEmployees()).willReturn(List.of(employee1, employee2));

        ResultActions response = mockMvc.perform(get("/api/employees"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].firstName", is("John")))
                .andExpect(jsonPath("$[1].firstName", is("Jane")));
    }

    @Test
    @DisplayName("GET /api/employees - Empty List Scenario (200 OK with Empty Array)")
    void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyList() throws Exception {
        given(employeeService.getAllEmployees()).willReturn(Collections.emptyList());

        ResultActions response = mockMvc.perform(get("/api/employees"));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    // ==========================================
    // 5. UPDATE EMPLOYEE (PUT /api/employees/{id})
    // ==========================================

    @Test
    @DisplayName("PUT /api/employees/{id} - Happy Scenario (200 OK)")
    void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        EmployeeDto updatedDto = new EmployeeDto(
                1L, "JohnUpdated", "DoeUpdated", "john.updated@example.com", "Engineering", new BigDecimal("90000.00")
        );
        given(employeeService.updateEmployee(eq(1L), any(EmployeeDto.class))).willReturn(updatedDto);

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDto)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("JohnUpdated")))
                .andExpect(jsonPath("$.department", is("Engineering")))
                .andExpect(jsonPath("$.salary", is(90000.00)));
    }

    @Test
    @DisplayName("PUT /api/employees/{id} - Sad Scenario (404 Not Found)")
    void givenInvalidEmployeeId_whenUpdateEmployee_thenReturn404NotFound() throws Exception {
        EmployeeDto updatedDto = getSampleEmployeeDto();
        given(employeeService.updateEmployee(eq(99L), any(EmployeeDto.class)))
                .willThrow(new ResourceNotFoundException("Employee", "id", 99L));

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedDto)));

        response.andExpect(status().isNotFound());
    }

    // ==========================================
    // 6. DELETE EMPLOYEE (DELETE /api/employees/{id})
    // ==========================================

    @Test
    @DisplayName("DELETE /api/employees/{id} - Happy Scenario (200 OK)")
    void givenEmployeeId_whenDeleteEmployee_thenReturn200OkStringResponse() throws Exception {
        willDoNothing().given(employeeService).deleteEmployee(1L);

        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", 1L));

        response.andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully."));
    }

    @Test
    @DisplayName("DELETE /api/employees/{id} - Sad Scenario (404 Not Found)")
    void givenInvalidEmployeeId_whenDeleteEmployee_thenReturn404NotFound() throws Exception {
        willThrow(new ResourceNotFoundException("Employee", "id", 99L))
                .given(employeeService).deleteEmployee(99L);

        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", 99L));

        response.andExpect(status().isNotFound());
    }
}
