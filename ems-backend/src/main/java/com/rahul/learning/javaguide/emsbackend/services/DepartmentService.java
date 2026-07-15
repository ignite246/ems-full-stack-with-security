package com.rahul.learning.javaguide.emsbackend.services;

import com.rahul.learning.javaguide.emsbackend.dtos.DepartmentDTO;

import java.util.List;

public interface DepartmentService {

    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);

    DepartmentDTO getDepartmentById(Long id);

    List<DepartmentDTO> getAllDepartments();

    void deleteDepartmentById(Long id);

    DepartmentDTO modifyDepartment(Long id, DepartmentDTO departmentDTO);
}
