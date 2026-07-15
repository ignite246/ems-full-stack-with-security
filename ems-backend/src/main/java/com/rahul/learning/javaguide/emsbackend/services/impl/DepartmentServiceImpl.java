package com.rahul.learning.javaguide.emsbackend.services.impl;

import com.rahul.learning.javaguide.emsbackend.dtos.DepartmentDTO;
import com.rahul.learning.javaguide.emsbackend.entities.Department;
import com.rahul.learning.javaguide.emsbackend.exceptions.ResourceNotFoundException;
import com.rahul.learning.javaguide.emsbackend.mappers.DepartmentMapper;
import com.rahul.learning.javaguide.emsbackend.repos.DepartmentRepository;
import com.rahul.learning.javaguide.emsbackend.services.DepartmentService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    @NonNull
    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        final Department department = DepartmentMapper.mapToDepartment(departmentDTO);
        final Department savedDepartment = departmentRepository.save(department);
        return DepartmentMapper.mapToDepartmentDTO(savedDepartment);
    }

    @Override
    public DepartmentDTO getDepartmentById(Long id) {
        final Department foundDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id=" + id));
        return DepartmentMapper.mapToDepartmentDTO(foundDepartment);
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        final List<Department> departments = departmentRepository.findAll();
        return departments.stream().map(DepartmentMapper::mapToDepartmentDTO).toList();
    }

    @Override
    public void deleteDepartmentById(Long id) {
        departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found with id=" + id));
        departmentRepository.deleteById(id);
    }

    @Override
    public DepartmentDTO modifyDepartment(Long id, DepartmentDTO departmentDTO) {
        final Department foundDepartment = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found with id=" + id));
        foundDepartment.setDepartmentName(departmentDTO.getDepartmentName());
        foundDepartment.setDepartmentDescription(departmentDTO.getDepartmentDescription());
        final Department updatedDepartment = departmentRepository.save(foundDepartment);
        return DepartmentMapper.mapToDepartmentDTO(updatedDepartment);
    }
}
