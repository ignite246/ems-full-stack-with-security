package com.rahul.learning.ems.backend.controllers;

import com.rahul.learning.ems.backend.dtos.JwtAuthResponseDTO;
import com.rahul.learning.ems.backend.dtos.LoginDTO;
import com.rahul.learning.ems.backend.dtos.RegisterDTO;
import com.rahul.learning.ems.backend.dtos.SuccessResponseDTO;
import com.rahul.learning.ems.backend.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="Auth APIs", description = "Auth APIs for user login and register")
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponseDTO> register(@Valid @RequestBody RegisterDTO registerDTO) {
        log.info("Register request received={}", registerDTO);
        final String response = authService.register(registerDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SuccessResponseDTO(response));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        log.info("Login request received={}", loginDTO);
        final JwtAuthResponseDTO jwtAuthResponseDTO = authService.login(loginDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jwtAuthResponseDTO);
    }
}