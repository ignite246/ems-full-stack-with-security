package com.rahul.learning.javaguide.emsbackend.services.impl;

import com.rahul.learning.javaguide.emsbackend.dtos.EmployeeDTO;
import com.rahul.learning.javaguide.emsbackend.entities.Department;
import com.rahul.learning.javaguide.emsbackend.entities.Employee;
import com.rahul.learning.javaguide.emsbackend.exceptions.ResourceNotFoundException;
import com.rahul.learning.javaguide.emsbackend.mappers.EmployeeMapper;
import com.rahul.learning.javaguide.emsbackend.repos.DepartmentRepository;
import com.rahul.learning.javaguide.emsbackend.repos.EmployeeRepository;
import com.rahul.learning.javaguide.emsbackend.services.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        final Employee employee = EmployeeMapper.mapToEmployee(employeeDTO);

        Department departmentFound =  departmentRepository.findById(employeeDTO.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found with id=" + employeeDTO.getDepartmentId()));
        employee.setDepartment(departmentFound);

        final Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDTO(savedEmployee);
    }

    @Override
    public EmployeeDTO getEmployeeById(Long employeeId) {
        Employee empFound = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee does not exist with given id : " + employeeId));

        return EmployeeMapper.mapToEmployeeDTO(empFound);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        final List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map((EmployeeMapper::mapToEmployeeDTO)).toList();
    }

    @Override
    public EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO updatedEmployeeDTO) {
        final Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist with given id : " + employeeId));

        Department departmentFound =  departmentRepository.findById(updatedEmployeeDTO.getDepartmentId()).orElseThrow(() -> new ResourceNotFoundException("Department not found with id=" + updatedEmployeeDTO.getDepartmentId()));

        existingEmployee.setDepartment(departmentFound);
        existingEmployee.setFirstName(updatedEmployeeDTO.getFirstName());
        existingEmployee.setLastName(updatedEmployeeDTO.getLastName());
        existingEmployee.setEmail(updatedEmployeeDTO.getEmail());

        final Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return EmployeeMapper.mapToEmployeeDTO(updatedEmployee);
    }

    @Override
    public void deleteEmployeeById(Long employeeId) {
        employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee does not exist with given id : " + employeeId));
        employeeRepository.deleteById(employeeId);
    }
}
