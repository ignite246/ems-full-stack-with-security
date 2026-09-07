package com.rahul.learning.javaguide.emsbackend.services;

import com.rahul.learning.javaguide.emsbackend.dtos.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO getEmployeeById(Long employeeId);
    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO employeeDTO);
    void deleteEmployeeById(Long employeeId);
}