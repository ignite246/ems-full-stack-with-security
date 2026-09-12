package com.rahul.learning.ems.backend.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "Employees' Experience Information")
public class ExperienceDTO {
    private Long expId;
    private String orgName;
    private LocalDate dateOfJoining;
    private LocalDate dateOfLeaving;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}