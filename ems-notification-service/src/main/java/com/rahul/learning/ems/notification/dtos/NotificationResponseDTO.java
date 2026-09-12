package com.rahul.learning.ems.notification.dtos;


import com.rahul.learning.ems.notification.enums.NotificationStatus;
import com.rahul.learning.ems.notification.enums.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponseDTO(
        Long notificationId,
        String recipient,
        String subject,
        String message,
        NotificationType type,
        NotificationStatus status,
        LocalDateTime createdAt,
        LocalDateTime sentAt
) {
}