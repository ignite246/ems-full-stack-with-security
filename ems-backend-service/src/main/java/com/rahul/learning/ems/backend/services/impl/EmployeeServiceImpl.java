package com.rahul.learning.ems.backend.services.impl;

import com.rahul.learning.ems.backend.dtos.EmployeeDTO;
import com.rahul.learning.ems.backend.entities.Department;
import com.rahul.learning.ems.backend.entities.Employee;
import com.rahul.learning.ems.backend.entities.Office;
import com.rahul.learning.ems.backend.exceptions.EmployeeCreationException;
import com.rahul.learning.ems.backend.exceptions.ResourceNotFoundException;
import com.rahul.learning.ems.backend.mappers.EmployeeMapper;
import com.rahul.learning.ems.backend.repos.DepartmentRepository;
import com.rahul.learning.ems.backend.repos.EmployeeRepository;
import com.rahul.learning.ems.backend.repos.OfficeRepository;
import com.rahul.learning.ems.backend.services.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;
    private final OfficeRepository officeRepository;

    @Transactional
    @CacheEvict(value = "employeeList", allEntries = true)
    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {

        final Employee employee = EmployeeMapper.mapToEmployee(employeeDTO);

        // Check if email already exists
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new EmployeeCreationException(
                    String.format(
                            "Employee cannot be created because email '%s' already exists.",
                            employeeDTO.getEmail()
                    )
            );
        }

        // Find department
        Department departmentFound =
                departmentRepository.findById(employeeDTO.getDepartmentId())
                        .orElseThrow(() ->
                                new EmployeeCreationException(
                                        String.format(
                                                "Employee cannot be created because department with id: '%s' not found.",
                                                employeeDTO.getDepartmentId()
                                        )
                                )
                        );

        employee.setDepartment(departmentFound);

        // Find office
        Office officeFound =
                officeRepository.findById(employeeDTO.getOfficeId())
                        .orElseThrow(() ->
                                new EmployeeCreationException(
                                        String.format(
                                                "Employee cannot be created because office with id: '%s' not found.",
                                                employeeDTO.getOfficeId()
                                        )
                                )
                        );

        employee.setOffice(officeFound);

        /*
         * CascadeType.ALL on Employee.experiences means
         * Experience entities will also be persisted.
         */
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
        final List<Employee> employees = employeeRepository.findAllWithDetails();
        return employees.stream()
                .map(EmployeeMapper::mapToEmployeeDTO)
                .toList();
    }

    @Transactional
    @Caching(
            put = @CachePut(value = "employees", key = "#employeeId"),
            evict = @CacheEvict(value = "employeeList", allEntries = true)
    )
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

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "employees", key = "#employeeId"),
                    @CacheEvict(value = "employeeList", allEntries = true)
            }
    )
    @Override
    public void deleteEmployeeById(Long employeeId) {
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist with given id : " + employeeId));
        employeeRepository.deleteById(employeeId);
    }
}
