package com.rahul.learning.ems.backend.events;


import com.rahul.learning.ems.backend.enums.EmployeeEventType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@Builder
public class EmployeeEvent {

    private final Long employeeId;
    private final String employeeName;
    private final String employeeEmail;
    private final EmployeeEventType eventType;
}
