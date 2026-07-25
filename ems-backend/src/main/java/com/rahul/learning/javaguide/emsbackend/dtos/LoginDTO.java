package com.rahul.learning.javaguide.emsbackend.dtos;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginDTO {
    private String usernameOrEmail;
    private String password;
}
