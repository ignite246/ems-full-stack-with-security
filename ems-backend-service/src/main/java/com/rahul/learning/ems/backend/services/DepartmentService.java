package com.rahul.learning.ems.backend.services;

import com.rahul.learning.ems.backend.dtos.DepartmentDTO;

import java.util.List;

public interface DepartmentService {

    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);

    DepartmentDTO getDepartmentById(Long id);

    List<DepartmentDTO> getAllDepartments();

    void deleteDepartmentById(Long id);

    DepartmentDTO modifyDepartment(Long id, DepartmentDTO departmentDTO);
}
