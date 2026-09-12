package com.rahul.learning.ems.backend.services;

import com.rahul.learning.ems.backend.dtos.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO getEmployeeById(Long employeeId);
    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO employeeDTO);
    void deleteEmployeeById(Long employeeId);
}