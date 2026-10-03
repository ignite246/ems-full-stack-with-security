package com.rahul.learning.ems.authservice.services;

import com.rahul.learning.ems.authservice.dtos.JwtAuthResponseDTO;
import com.rahul.learning.ems.authservice.dtos.LoginDTO;
import com.rahul.learning.ems.authservice.dtos.RegisterDTO;

public interface AuthService {

    String register(RegisterDTO registerDTO);

    JwtAuthResponseDTO login(LoginDTO loginDTO);
}