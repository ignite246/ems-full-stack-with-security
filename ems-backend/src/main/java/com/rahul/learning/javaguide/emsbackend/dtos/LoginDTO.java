package com.rahul.learning.javaguide.emsbackend.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(

        @NotBlank(message = "Username or Email cannot be blank")
        String usernameOrEmail,

        @NotBlank(message = "Password is required")
        String password
) {
}
