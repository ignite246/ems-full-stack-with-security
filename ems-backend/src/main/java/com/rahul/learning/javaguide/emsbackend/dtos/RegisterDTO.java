package com.rahul.learning.javaguide.emsbackend.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegisterDTO {
    private String name;
    private String email;
    private String username;
    private String password;
    private String confirmPassword;
}
