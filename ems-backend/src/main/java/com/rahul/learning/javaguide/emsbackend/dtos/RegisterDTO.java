package com.rahul.learning.javaguide.emsbackend.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Schema(description = "User Registration Information")
public class RegisterDTO {

    @NotBlank(message = "name is required")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name should contain only alphabets")
    private String name;

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must contain min 4 and max 20 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must contain at least 6 characters")
    private String password;

    // TODO
    private String confirmPassword;
}
