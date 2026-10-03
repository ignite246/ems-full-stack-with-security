package com.rahul.learning.ems.authservice.controllers;

import com.rahul.learning.ems.authservice.dtos.JwtAuthResponseDTO;
import com.rahul.learning.ems.authservice.dtos.LoginDTO;
import com.rahul.learning.ems.authservice.dtos.RegisterDTO;
import com.rahul.learning.ems.authservice.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDTO registerDTO) {

        final String response = authService.register(registerDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {

        final JwtAuthResponseDTO response = authService.login(loginDTO);

        return ResponseEntity.ok(response);
    }
}