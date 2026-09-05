package com.example.testing.service.impl;

import com.example.testing.dto.EmployeeDto;
import com.example.testing.entities.Employee;
import com.example.testing.exceptions.ResourceNotFoundException;
import com.example.testing.repositories.EmployeeRepository;
import com.example.testing.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    @Transactional
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        log.info("Creating new employee with email: {}", employeeDto.email());
        if (employeeRepository.existsByEmail(employeeDto.email())) {
            throw new IllegalArgumentException("Employee with email " + employeeDto.email() + " already exists.");
        }

        Employee employee = mapToEntity(employeeDto);
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Successfully created employee with ID: {}", savedEmployee.getId());
        return mapToDto(savedEmployee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeById(Long employeeId) {
        log.info("Fetching employee with ID: {}", employeeId);
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        return mapToDto(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeByEmail(String email) {
        log.info("Fetching employee with email: {}", email);
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "email", email));
        return mapToDto(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDto> getAllEmployees() {
        log.info("Fetching all employees");
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeDto updateEmployee(Long employeeId, EmployeeDto updatedEmployeeDto) {
        log.info("Updating employee with ID: {}", employeeId);
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));

        employee.setFirstName(updatedEmployeeDto.firstName());
        employee.setLastName(updatedEmployeeDto.lastName());
        employee.setEmail(updatedEmployeeDto.email());
        employee.setDepartment(updatedEmployeeDto.department());
        employee.setSalary(updatedEmployeeDto.salary());

        Employee updatedEmployee = employeeRepository.save(employee);
        log.info("Successfully updated employee with ID: {}", updatedEmployee.getId());
        return mapToDto(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long employeeId) {
        log.info("Deleting employee with ID: {}", employeeId);
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", employeeId));
        employeeRepository.delete(employee);
        log.info("Successfully deleted employee with ID: {}", employeeId);
    }

    // Entity <-> DTO Mappings
    private EmployeeDto mapToDto(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getSalary()
        );
    }

    private Employee mapToEntity(EmployeeDto dto) {
        return new Employee(
                dto.id(),
                dto.firstName(),
                dto.lastName(),
                dto.email(),
                dto.department(),
                dto.salary()
        );
    }
}
