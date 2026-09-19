package com.rahul.learning.ems.backend.clients;

import com.rahul.learning.ems.backend.dtos.notifications.NotificationRequestDTO;
import com.rahul.learning.ems.backend.dtos.notifications.NotificationServiceRequestDTO;
import com.rahul.learning.ems.backend.enums.EmployeeEventType;
import com.rahul.learning.ems.backend.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Log4j2
@RequiredArgsConstructor
@Service
public class NotificationClientImpl implements NotificationClient {

    private final RestClient notificationRestClient;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    @Value("${notification.retry.max-attempts:3}")
    private int maxAttempts;

    @Value("${notification.retry.backoff-delay:1000}")
    private int retryBackoffDelay;

    @Override
    @Retryable(
            retryFor = ResourceAccessException.class,
            maxAttemptsExpression = "${notification.retry.max-attempts:3}",
            backoff = @Backoff(delayExpression = "${notification.retry.backoff-delay:1000}")
    )
    public void send(NotificationRequestDTO request) {

        RetryContext context = RetrySynchronizationManager.getContext();
        int attempt = context != null ? context.getRetryCount() + 1 : 1;

        log.info(
                "Notification delivery attempt started. employeeId={}, eventType={}, attempt={}",
                request.getEmployeeId(),
                request.getEventType(),
                attempt
        );

        NotificationServiceRequestDTO notificationRequest =
                NotificationServiceRequestDTO.builder()
                        .recipient(request.getEmployeeEmail())
                        .subject(getSubject(request.getEventType()))
                        .message(getMessage(request))
                        .type(NotificationType.valueOf(request.getEventType().name()))
                        .build();

        String endpoint = notificationServiceUrl + "/api/notifications";

        log.info(
                "Sending notification request. employeeId={}, eventType={}, endpoint={}, attempt={}",
                request.getEmployeeId(),
                request.getEventType(),
                endpoint,
                attempt
        );

        try {

            ResponseEntity<Void> responseEntity = notificationRestClient
                    .post()
                    .uri(endpoint)
                    .body(notificationRequest)
                    .retrieve()
                    .toBodilessEntity();

            log.info(
                    "Notification delivered successfully. employeeId={}, eventType={}, status={}, attempt={}",
                    request.getEmployeeId(),
                    request.getEventType(),
                    responseEntity.getStatusCode(),
                    attempt
            );

        } catch (HttpClientErrorException ex) {

            log.warn(
                    "Notification request rejected by Notification Service. employeeId={}, eventType={}, status={}, attempt={}",
                    request.getEmployeeId(),
                    request.getEventType(),
                    ex.getStatusCode(),
                    attempt
            );

            throw ex;

        } catch (HttpServerErrorException ex) {

            log.warn(
                    "Notification Service returned server error. " +
                            "employeeId={}, eventType={}, status={}, attempt={}",
                    request.getEmployeeId(),
                    request.getEventType(),
                    ex.getStatusCode(),
                    attempt
            );

            throw ex;
        }
    }

    @Recover
    public void recover(ResourceAccessException exception, NotificationRequestDTO request) {

        log.error(
                "Notification delivery failed after all retry attempts. employeeId={}, eventType={}, attempts={}, backOffDelay={}",
                request.getEmployeeId(),
                request.getEventType(),
                maxAttempts,
                retryBackoffDelay,
                exception
        );
    }

    private String getSubject(EmployeeEventType eventType) {
        return switch (eventType) {
            case EMPLOYEE_CREATED -> "Employee Created";
            case EMPLOYEE_UPDATED -> "Employee Updated";
            case EMPLOYEE_DELETED -> "Employee Deleted";
        };
    }

    private String getMessage(NotificationRequestDTO request) {
        return switch (request.getEventType()) {
            case EMPLOYEE_CREATED -> "Employee " + request.getEmployeeName() + " has been successfully created.";
            case EMPLOYEE_UPDATED -> "Employee " + request.getEmployeeName() + " has been successfully updated.";
            case EMPLOYEE_DELETED -> "Employee " + request.getEmployeeName() + " has been successfully deleted.";
            // default -> "An employee notification has been generated."; //not required
        };
    }
}