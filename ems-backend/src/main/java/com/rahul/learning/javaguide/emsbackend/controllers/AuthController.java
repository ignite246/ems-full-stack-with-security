package com.rahul.learning.javaguide.emsbackend.controllers;

import com.rahul.learning.javaguide.emsbackend.dtos.LoginDTO;
import com.rahul.learning.javaguide.emsbackend.dtos.RegisterDTO;
import com.rahul.learning.javaguide.emsbackend.records.SuccessResponseDTO;
import com.rahul.learning.javaguide.emsbackend.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponseDTO> register(@RequestBody RegisterDTO registerDTO) {
        log.info("Register request received={}", registerDTO);
        final String response = authService.register(registerDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SuccessResponseDTO(response));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        log.info("Login request received={}", loginDTO);
        final String loginResponse = authService.login(loginDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new SuccessResponseDTO(loginResponse));
    }
}