package com.rahul.learning.javaguide.emsbackend.controllers;

import com.rahul.learning.javaguide.emsbackend.dtos.EmployeeDTO;
import com.rahul.learning.javaguide.emsbackend.dtos.OfficeDTO;
import com.rahul.learning.javaguide.emsbackend.services.OfficeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/offices")
public class OfficeController {

    private final OfficeService officeService;

    @PostMapping
    public ResponseEntity<OfficeDTO> createOffice(@RequestBody OfficeDTO officeDTO) {
        log.info("Payload to create an office: {}", officeDTO);
        OfficeDTO createdOffice = officeService.createOffice(officeDTO);
        return ResponseEntity.ok().body(createdOffice);
    }

    @GetMapping
    public ResponseEntity<List<OfficeDTO>> getAllOffices() {
        log.info("Get all offices");
        List<OfficeDTO> getAllOffice = officeService.getAllOffices();
        return ResponseEntity.ok().body(getAllOffice);
    }
}
