package com.example.testing.repositories;

import com.example.testing.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setSalary(BigDecimal.valueOf(100L));
        employee.setEmail("johndoe@gmail.com");
    }

    @Test
    void findByEmail_whenEmailIsValid_thenReturnEmployee() {
        // Arrange, Given
        employeeRepository.save(employee);

        // Act, When
        Optional<Employee> employees = employeeRepository.findByEmail(employee.getEmail());

        // Assert, Then
        assertThat(employees).isPresent();
        assertThat(employees.get().getFirstName()).isEqualTo("John");
        assertThat(employees.get().getLastName()).isEqualTo("Doe");
        assertThat(employees.get().getSalary()).isEqualByComparingTo(BigDecimal.valueOf(100L));
        assertThat(employees.get().getEmail()).isEqualTo("johndoe@gmail.com");
    }

    @Test
    void findByEmail_whenEmailIsNotValid_thenReturnNull() {
        // Arrange, Given
        employeeRepository.save(employee);

        // Act, When
        Optional<Employee> employees = employeeRepository.findByEmail("invalid");

        // Assert, Then
        assertThat(employees).isNotPresent();
    }

    @Test
    void existsByEmail() {
    }
}