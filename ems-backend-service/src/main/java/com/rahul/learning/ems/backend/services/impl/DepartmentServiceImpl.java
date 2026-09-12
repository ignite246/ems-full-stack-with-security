package com.rahul.learning.ems.backend.services.impl;

import com.rahul.learning.ems.backend.dtos.DepartmentDTO;
import com.rahul.learning.ems.backend.entities.Department;
import com.rahul.learning.ems.backend.exceptions.DepartmentDeletionException;
import com.rahul.learning.ems.backend.exceptions.ResourceNotFoundException;
import com.rahul.learning.ems.backend.mappers.DepartmentMapper;
import com.rahul.learning.ems.backend.repos.DepartmentRepository;
import com.rahul.learning.ems.backend.repos.EmployeeRepository;
import com.rahul.learning.ems.backend.services.DepartmentService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    @NonNull
    private final DepartmentRepository departmentRepository;
    @NonNull
    private final EmployeeRepository employeeRepository;

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
        departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id=" + id));

        if(employeeRepository.existsByDepartmentId(id)){
            throw new DepartmentDeletionException(
                    "Department cannot be deleted because employees are assigned to it.");
        }
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
