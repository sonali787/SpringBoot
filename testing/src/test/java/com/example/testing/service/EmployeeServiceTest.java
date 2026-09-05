package com.example.testing.service;

import com.example.testing.dto.EmployeeDto;
import com.example.testing.entities.Employee;
import com.example.testing.exceptions.ResourceNotFoundException;
import com.example.testing.repositories.EmployeeRepository;
import com.example.testing.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Captor
    private ArgumentCaptor<Employee> employeeArgumentCaptor;

    private Employee employee;
    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        employee = new Employee(1L, "John", "Doe", "john.doe@example.com", "IT", new BigDecimal("75000.00"));
        employeeDto = new EmployeeDto(1L, "John", "Doe", "john.doe@example.com", "IT", new BigDecimal("75000.00"));
    }

    // ==========================================
    // 1. CREATE EMPLOYEE TESTS
    // ==========================================

    @Test
    @DisplayName("JUnit test for createEmployee method - Happy Scenario")
    void givenEmployeeDto_whenCreateEmployee_thenReturnSavedEmployeeDto() {
        // Arrange
        when(employeeRepository.existsByEmail(employeeDto.email())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        EmployeeDto savedDto = employeeService.createEmployee(employeeDto);

        // Assert
        assertThat(savedDto).isNotNull();
        assertThat(savedDto.id()).isEqualTo(1L);
        assertThat(savedDto.firstName()).isEqualTo("John");
        assertThat(savedDto.email()).isEqualTo("john.doe@example.com");

        // Verify
        verify(employeeRepository, times(1)).existsByEmail(employeeDto.email());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("JUnit test for createEmployee method - Sad Scenario (Email already exists)")
    void givenExistingEmail_whenCreateEmployee_thenThrowsIllegalArgumentException() {
        // Arrange
        when(employeeRepository.existsByEmail(employeeDto.email())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> employeeService.createEmployee(employeeDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Employee with email " + employeeDto.email() + " already exists.");

        // Verify - save() must NEVER be called
        verify(employeeRepository, times(1)).existsByEmail(employeeDto.email());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("JUnit test using ArgumentCaptor to capture argument passed to repository.save()")
    void givenEmployeeDto_whenCreateEmployee_thenCapturesSavedEntity() {
        // Arrange
        when(employeeRepository.existsByEmail(employeeDto.email())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        // Act
        employeeService.createEmployee(employeeDto);

        // Verify and Capture
        verify(employeeRepository).save(employeeArgumentCaptor.capture());
        Employee capturedEmployee = employeeArgumentCaptor.getValue();

        // Assert mapped fields
        assertThat(capturedEmployee).isNotNull();
        assertThat(capturedEmployee.getFirstName()).isEqualTo("John");
        assertThat(capturedEmployee.getLastName()).isEqualTo("Doe");
        assertThat(capturedEmployee.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(capturedEmployee.getDepartment()).isEqualTo("IT");
    }

    // ==========================================
    // 2. GET EMPLOYEE BY ID TESTS
    // ==========================================

    @Test
    @DisplayName("JUnit test for getEmployeeById method - Happy Scenario")
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeDto() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Act
        EmployeeDto foundDto = employeeService.getEmployeeById(1L);

        // Assert
        assertThat(foundDto).isNotNull();
        assertThat(foundDto.id()).isEqualTo(1L);
        assertThat(foundDto.firstName()).isEqualTo("John");
        assertThat(foundDto.email()).isEqualTo("john.doe@example.com");

        // Verify
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("JUnit test for getEmployeeById method - Sad Scenario (ID not found)")
    void givenInvalidEmployeeId_whenGetEmployeeById_thenThrowsResourceNotFoundException() {
        // Arrange
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> employeeService.getEmployeeById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employee not found with id : '99'");

        // Verify
        verify(employeeRepository, times(1)).findById(99L);
    }

    // ==========================================
    // 3. GET EMPLOYEE BY EMAIL TESTS
    // ==========================================

    @Test
    @DisplayName("JUnit test for getEmployeeByEmail method - Happy Scenario")
    void givenEmployeeEmail_whenGetEmployeeByEmail_thenReturnEmployeeDto() {
        // Arrange
        when(employeeRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(employee));

        // Act
        EmployeeDto foundDto = employeeService.getEmployeeByEmail("john.doe@example.com");

        // Assert
        assertThat(foundDto).isNotNull();
        assertThat(foundDto.email()).isEqualTo("john.doe@example.com");

        // Verify
        verify(employeeRepository, times(1)).findByEmail("john.doe@example.com");
    }

    @Test
    @DisplayName("JUnit test for getEmployeeByEmail method - Sad Scenario (Email not found)")
    void givenInvalidEmail_whenGetEmployeeByEmail_thenThrowsResourceNotFoundException() {
        // Arrange
        String invalidEmail = "nonexistent@example.com";
        when(employeeRepository.findByEmail(invalidEmail)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> employeeService.getEmployeeByEmail(invalidEmail))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employee not found with email : 'nonexistent@example.com'");

        // Verify
        verify(employeeRepository, times(1)).findByEmail(invalidEmail);
    }

    // ==========================================
    // 4. GET ALL EMPLOYEES TESTS
    // ==========================================

    @Test
    @DisplayName("JUnit test for getAllEmployees method - Happy Scenario")
    void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeeList() {
        // Arrange
        Employee employee2 = new Employee(2L, "Jane", "Smith", "jane.smith@example.com", "HR", new BigDecimal("80000.00"));
        when(employeeRepository.findAll()).thenReturn(List.of(employee, employee2));

        // Act
        List<EmployeeDto> employeeList = employeeService.getAllEmployees();

        // Assert
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).hasSize(2);
        assertThat(employeeList.get(0).firstName()).isEqualTo("John");
        assertThat(employeeList.get(1).firstName()).isEqualTo("Jane");

        // Verify
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("JUnit test for getAllEmployees method - Empty List Scenario")
    void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyList() {
        // Arrange
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<EmployeeDto> employeeList = employeeService.getAllEmployees();

        // Assert
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isEmpty();

        // Verify
        verify(employeeRepository, times(1)).findAll();
    }

    // ==========================================
    // 5. UPDATE EMPLOYEE TESTS
    // ==========================================

    @Test
    @DisplayName("JUnit test for updateEmployee method - Happy Scenario")
    void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // Arrange
        EmployeeDto updatedDto = new EmployeeDto(1L, "JohnUpdated", "DoeUpdated", "john.updated@example.com", "Engineering", new BigDecimal("90000.00"));
        Employee updatedEntity = new Employee(1L, "JohnUpdated", "DoeUpdated", "john.updated@example.com", "Engineering", new BigDecimal("90000.00"));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEntity);

        // Act
        EmployeeDto resultDto = employeeService.updateEmployee(1L, updatedDto);

        // Assert
        assertThat(resultDto).isNotNull();
        assertThat(resultDto.firstName()).isEqualTo("JohnUpdated");
        assertThat(resultDto.email()).isEqualTo("john.updated@example.com");
        assertThat(resultDto.department()).isEqualTo("Engineering");
        assertThat(resultDto.salary()).isEqualTo(new BigDecimal("90000.00"));

        // Verify
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("JUnit test for updateEmployee method - Sad Scenario (Employee ID not found)")
    void givenInvalidEmployeeId_whenUpdateEmployee_thenThrowsResourceNotFoundException() {
        // Arrange
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> employeeService.updateEmployee(99L, employeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employee not found with id : '99'");

        // Verify - save() must NEVER be called
        verify(employeeRepository, times(1)).findById(99L);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // ==========================================
    // 6. DELETE EMPLOYEE TESTS
    // ==========================================

    @Test
    @DisplayName("JUnit test for deleteEmployee method - Happy Scenario")
    void givenEmployeeId_whenDeleteEmployee_thenEmployeeIsDeleted() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Act
        employeeService.deleteEmployee(1L);

        // Verify - delete(employee) must be called once
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    @DisplayName("JUnit test for deleteEmployee method - Sad Scenario (Employee ID not found)")
    void givenInvalidEmployeeId_whenDeleteEmployee_thenThrowsResourceNotFoundException() {
        // Arrange
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> employeeService.deleteEmployee(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Employee not found with id : '99'");

        // Verify - delete() must NEVER be called
        verify(employeeRepository, times(1)).findById(99L);
        verify(employeeRepository, never()).delete(any(Employee.class));
    }
}
