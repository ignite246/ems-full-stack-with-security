package com.rahul.learning.javaguide.emsbackend.controllers;

import com.rahul.learning.javaguide.emsbackend.configs.swagger.AdminApiResponses;
import com.rahul.learning.javaguide.emsbackend.configs.swagger.NotFoundApiResponse;
import com.rahul.learning.javaguide.emsbackend.configs.swagger.SecuredApiResponses;
import com.rahul.learning.javaguide.emsbackend.dtos.EmployeeDTO;
import com.rahul.learning.javaguide.emsbackend.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Tag(name = "Employee APIs", description = "APIs for managing employees")
@CrossOrigin("*")
@Log4j2
@AllArgsConstructor
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Operation(summary = "Create employee", description = "Creates a new employee. Requires ADMIN role.")
    @AdminApiResponses
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("STARTS::createEmployee::employeeDTORequest={}", objectMapper.writeValueAsString(employeeDTO));
        EmployeeDTO savedEmployeeDTO = employeeService.createEmployee(employeeDTO);
        return new ResponseEntity<>(savedEmployeeDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get employee by ID", description = "Retrieves an employee using the employee ID.")
    @SecuredApiResponses
    @NotFoundApiResponse
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@Parameter(
            description = "Unique ID of the employee",
            example = "25"
    ) @PathVariable("id") Long employeeId) {
        log.info("STARTS::getEmployeeById::employeeId={}", employeeId);
        EmployeeDTO employeeDTO = employeeService.getEmployeeById(employeeId);
        return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get all employees", description = "Retrieves all employees.")
    @SecuredApiResponses
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        log.info("STARTS::getAllEmployees");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            log.info("Authenticated user name: {}", authentication.getName());
        }
        List<EmployeeDTO> employeeDTOs = employeeService.getAllEmployees();
        log.info("ENDS::getAllEmployees::employeeList={}", objectMapper.writeValueAsString(employeeDTOs));
        return new ResponseEntity<>(employeeDTOs, HttpStatus.OK);
    }

    @Operation(summary = "Update employee", description = "Updates an existing employee. Requires ADMIN role.")
    @AdminApiResponses
    @NotFoundApiResponse
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable("id") Long employeeId, @RequestBody EmployeeDTO employeeDTO) {
        log.info("STARTS::updateEmployee::employeeDTORequest and employeeId={}...{}", employeeId, objectMapper.writeValueAsString(employeeDTO));
        final EmployeeDTO updatedEmployeeDTO = employeeService.updateEmployee(employeeId, employeeDTO);
        return ResponseEntity.ok(updatedEmployeeDTO);
    }

    @Operation(summary = "Delete employee", description = "Deletes an existing employee. Requires ADMIN role.")
    @AdminApiResponses
    @NotFoundApiResponse
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employeeId) {
        log.info("STARTS::deleteEmployee::employeeId={}", employeeId);
        employeeService.deleteEmployeeById(employeeId);
        return ResponseEntity.ok("Employee has been deleted; id=" + employeeId);
    }
}