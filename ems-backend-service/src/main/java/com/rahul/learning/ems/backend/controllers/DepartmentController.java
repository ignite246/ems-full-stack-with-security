package com.rahul.learning.ems.backend.controllers;

import com.rahul.learning.ems.backend.dtos.DepartmentDTO;
import com.rahul.learning.ems.backend.services.DepartmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Department APIs", description = "APIs for managing departments")
@CrossOrigin("*")
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        final DepartmentDTO createdDepartment = departmentService.createDepartment(departmentDTO);
        return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable("id") Long id) {
        final DepartmentDTO foundDepartmentDTO = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(foundDepartmentDTO);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        final List<DepartmentDTO> departmentDTOS = departmentService.getAllDepartments();
        return ResponseEntity.ok(departmentDTOS);
    }

    @PutMapping("{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable("id") Long id, @RequestBody DepartmentDTO departmentDTO) {
        final DepartmentDTO modifiedDepartment = departmentService.modifyDepartment(id, departmentDTO);
        return ResponseEntity.ok(modifiedDepartment);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable("id") Long id) {
        log.info("Delete department with id={}", id);
        departmentService.deleteDepartmentById(id);
        return new ResponseEntity<>("Department deleted with id=" + id, HttpStatus.OK);
    }
}
