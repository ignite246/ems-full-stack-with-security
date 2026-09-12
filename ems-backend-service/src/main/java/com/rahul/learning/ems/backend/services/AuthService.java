package com.rahul.learning.ems.backend.services;

import com.rahul.learning.ems.backend.dtos.JwtAuthResponseDTO;
import com.rahul.learning.ems.backend.dtos.LoginDTO;
import com.rahul.learning.ems.backend.dtos.RegisterDTO;

public interface AuthService {
    String register(RegisterDTO registerDTO);

    JwtAuthResponseDTO login(LoginDTO loginDTO);
}