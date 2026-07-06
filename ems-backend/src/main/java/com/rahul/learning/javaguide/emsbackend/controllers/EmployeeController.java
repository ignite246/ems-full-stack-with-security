package com.rahul.learning.javaguide.emsbackend.controllers;

import com.rahul.learning.javaguide.emsbackend.dtos.EmployeeDTO;
import com.rahul.learning.javaguide.emsbackend.services.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("STARTS::createEmployee::employeeDTORequest={}", objectMapper.writeValueAsString(employeeDTO));
        EmployeeDTO savedEmployeeDTO = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(savedEmployeeDTO, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("id") Long employeeId) {
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(employeeId);
        return new ResponseEntity<>(employeeDTO, HttpStatus.FOUND);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employeeDTOs = employeeService.getAllEmployees();
        return new ResponseEntity<>(employeeDTOs, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable("id") Long employeeId, @RequestBody EmployeeDTO employeeDTO) {
        log.info("STARTS::updateEmployee::employeeDTORequest and employeeId={}...{}", employeeId, objectMapper.writeValueAsString(employeeDTO));
        final EmployeeDTO updatedEmployeeDTO = employeeService.updateEmployee(employeeId, employeeDTO);
        return ResponseEntity.ok(updatedEmployeeDTO);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId) {
        log.info("STARTS::deleteEmployee::employeeId={}", employeeId);
        employeeService.deleteEmployeeById(employeeId);
        return ResponseEntity.ok("Employee has been deleted; id=" + employeeId);
    }
}