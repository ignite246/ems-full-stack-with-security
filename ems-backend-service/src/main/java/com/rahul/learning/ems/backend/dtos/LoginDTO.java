package com.rahul.learning.ems.backend.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(

        @NotBlank(message = "Username or Email cannot be blank")
        String usernameOrEmail,

        @NotBlank(message = "Password is required")
        String password
) {
}
