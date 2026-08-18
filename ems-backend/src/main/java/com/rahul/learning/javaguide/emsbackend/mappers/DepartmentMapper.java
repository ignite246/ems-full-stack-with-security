package com.rahul.learning.javaguide.emsbackend.mappers;

import com.rahul.learning.javaguide.emsbackend.dtos.DepartmentDTO;
import com.rahul.learning.javaguide.emsbackend.entities.Department;

public class DepartmentMapper {
    private DepartmentMapper() {
        /* This utility class should not be instantiated */
    }


    public static DepartmentDTO mapToDepartmentDTO(Department department) {
        return new DepartmentDTO(
                department.getId(),
                department.getDepartmentName(),
                department.getDepartmentDescription(),
                department.getCreatedAt(),
                department.getUpdatedAt()
        );
    }

    public static Department mapToDepartment(DepartmentDTO departmentDTO) {
        return new Department(
                departmentDTO.getId(),
                departmentDTO.getDepartmentName(),
                departmentDTO.getDepartmentDescription(),
                departmentDTO.getCreatedAt(),
                departmentDTO.getCreatedAt()
        );
    }
}
