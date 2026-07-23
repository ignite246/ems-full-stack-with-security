package com.rahul.learning.javaguide.emsbackend.services;

import com.rahul.learning.javaguide.emsbackend.dtos.RegisterDTO;

public interface AuthService {

    String register(RegisterDTO registerDTO);
}
