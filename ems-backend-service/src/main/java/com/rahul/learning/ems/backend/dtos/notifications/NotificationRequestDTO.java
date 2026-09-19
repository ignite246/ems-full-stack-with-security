package com.rahul.learning.ems.backend.dtos.notifications;

import com.rahul.learning.ems.backend.enums.EmployeeEventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {

    private Long employeeId;

    private String employeeName;

    private String employeeEmail;

    private EmployeeEventType eventType;
}
