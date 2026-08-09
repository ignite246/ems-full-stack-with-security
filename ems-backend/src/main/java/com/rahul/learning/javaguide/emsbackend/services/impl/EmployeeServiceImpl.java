package com.rahul.learning.javaguide.emsbackend.services.impl;

import com.rahul.learning.javaguide.emsbackend.dtos.EmployeeDTO;
import com.rahul.learning.javaguide.emsbackend.entities.Department;
import com.rahul.learning.javaguide.emsbackend.entities.Employee;
import com.rahul.learning.javaguide.emsbackend.exceptions.EmployeeCreationException;
import com.rahul.learning.javaguide.emsbackend.exceptions.ResourceNotFoundException;
import com.rahul.learning.javaguide.emsbackend.mappers.EmployeeMapper;
import com.rahul.learning.javaguide.emsbackend.repos.DepartmentRepository;
import com.rahul.learning.javaguide.emsbackend.repos.EmployeeRepository;
import com.rahul.learning.javaguide.emsbackend.services.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;

    @CacheEvict(value = "employeeList", allEntries = true)
    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        final Employee employee = EmployeeMapper.mapToEmployee(employeeDTO);

        //Check if email already exist
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new EmployeeCreationException(
                    String.format(
                            "Employee cannot be created because email '%s' already exists.",
                            employeeDTO.getEmail()
                    )
            );
        }

        //Find the department and set it onto employee or throw exception if not found
        Department departmentFound = departmentRepository.findById(employeeDTO.getDepartmentId())
                .orElseThrow(() -> new EmployeeCreationException(
                        String.format("Employee cannot be created because department with id: '%s' not found.",
                                employeeDTO.getDepartmentId())
                ));

        employee.setDepartment(departmentFound);

        final Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDTO(savedEmployee);
    }

    @Cacheable(value = "employees", key = "#employeeId")
    @Override
    public EmployeeDTO getEmployeeById(Long employeeId) {
        Employee empFound = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exist with given id : " + employeeId));

        return EmployeeMapper.mapToEmployeeDTO(empFound);
    }

    @Cacheable(value = "employeeList")
    @Override
    public List<EmployeeDTO> getAllEmployees() {
        final List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map((EmployeeMapper::mapToEmployeeDTO)).toList();
    }

    @CachePut(value = "employees", key = "#employeeId")
    @Override
    public EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO updatedEmployeeDTO) {
        final Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist with given id : " + employeeId));

        Department departmentFound = departmentRepository.findById(updatedEmployeeDTO.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found with id=" + updatedEmployeeDTO.getDepartmentId()));

        existingEmployee.setDepartment(departmentFound);
        existingEmployee.setFirstName(updatedEmployeeDTO.getFirstName());
        existingEmployee.setLastName(updatedEmployeeDTO.getLastName());
        existingEmployee.setEmail(updatedEmployeeDTO.getEmail());

        final Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return EmployeeMapper.mapToEmployeeDTO(updatedEmployee);
    }

    @CacheEvict(value = "employees", allEntries = true)
    @Override
    public void deleteEmployeeById(Long employeeId) {
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist with given id : " + employeeId));
        employeeRepository.deleteById(employeeId);
    }
}
