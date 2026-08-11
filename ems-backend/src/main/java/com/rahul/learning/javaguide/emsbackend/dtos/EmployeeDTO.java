package com.rahul.learning.javaguide.emsbackend.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Employee information")
public class EmployeeDTO {
    private Long id;

    @Schema(description = "Employee's first name", example = "Rahul")
    private String firstName;

    @Schema(description = "Employee's last name", example = "Kumar")
    private String lastName;

    @Schema(description = "Employee's email id", example = "abc123@domain.com")
    private String email;

    @Schema(description = "Employee's departmentId", example = "1")
    private Long departmentId;
}