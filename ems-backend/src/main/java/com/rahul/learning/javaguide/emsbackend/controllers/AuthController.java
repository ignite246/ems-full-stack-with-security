package com.rahul.learning.javaguide.emsbackend.controllers;

import com.rahul.learning.javaguide.emsbackend.dtos.JwtAuthResponseDTO;
import com.rahul.learning.javaguide.emsbackend.dtos.LoginDTO;
import com.rahul.learning.javaguide.emsbackend.dtos.RegisterDTO;
import com.rahul.learning.javaguide.emsbackend.dtos.SuccessResponseDTO;
import com.rahul.learning.javaguide.emsbackend.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
        final String token = authService.login(loginDTO);

        JwtAuthResponseDTO jwtAuthResponse = new JwtAuthResponseDTO();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jwtAuthResponse);
    }
}