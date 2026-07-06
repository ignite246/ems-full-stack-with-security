package com.rahul.learning.javaguide.emsbackend.mappers;

import com.rahul.learning.javaguide.emsbackend.dtos.EmployeeDTO;
import com.rahul.learning.javaguide.emsbackend.entities.Employee;

public class EmployeeMapper {

    private EmployeeMapper() {
        /* This utility class should not be instantiated */
    }


    public static EmployeeDTO mapToEmployeeDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail()
        );
    }

    public static Employee mapToEmployee(EmployeeDTO employeeDTO) {
        return new Employee(employeeDTO.getId(),
                employeeDTO.getFirstName(),
                employeeDTO.getLastName(),
                employeeDTO.getEmail()
        );
    }
}
