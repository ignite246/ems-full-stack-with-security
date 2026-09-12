package com.rahul.learning.ems.backend.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DepartmentDTO {
    private Long id;
    private String departmentName;
    private String departmentDescription;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}