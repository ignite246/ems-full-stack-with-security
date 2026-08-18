package com.rahul.learning.javaguide.emsbackend.dtos;

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