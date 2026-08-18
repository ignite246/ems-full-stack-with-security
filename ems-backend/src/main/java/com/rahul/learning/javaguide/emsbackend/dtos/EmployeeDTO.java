package com.rahul.learning.javaguide.emsbackend.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Employee Information")
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

    @Schema(description = "Employee's officeId", example = "1")
    private Long officeId;

    @Schema(description = "Employee's list of experiences", example = """
        [
          {
            "orgName": "TCS",
            "dateOfJoining": "2020-07-01T09:00:00",
            "dateOfLeaving": "2023-06-30T18:00:00"
          },
          {
            "orgName": "Infosys",
            "dateOfJoining": "2023-07-03T09:00:00",
            "dateOfLeaving": null
          }
        ]
        """
    )
    private List<ExperienceDTO> experiences;

    @Schema(description = "Employee creation local date time", example = "10/02/2026 12:10:143")
    private LocalDateTime createdAt;

    @Schema(description = "Employee last update local date time", example = "10/02/2026 12:10:143")
    private LocalDateTime updatedAt;
}