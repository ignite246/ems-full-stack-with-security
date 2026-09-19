package com.rahul.learning.ems.backend.events;

import com.rahul.learning.ems.backend.clients.NotificationClient;
import com.rahul.learning.ems.backend.dtos.notifications.NotificationRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Log4j2
@Component
@RequiredArgsConstructor
public class EmployeeEventListener {

    private final NotificationClient notificationClient;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmployeeEvent(EmployeeEvent event) {

        log.info(
                "Processing employee event after transaction commit. employeeId={}, eventType={}",
                event.getEmployeeId(),
                event.getEventType()
        );

        NotificationRequestDTO request = NotificationRequestDTO.builder()
                .employeeId(event.getEmployeeId())
                .employeeName(event.getEmployeeName())
                .employeeEmail(event.getEmployeeEmail())
                .eventType(event.getEventType())
                .build();

        notificationClient.send(request);
    }
}
