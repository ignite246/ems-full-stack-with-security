package com.rahul.learning.ems.backend.dtos.notifications;

import com.rahul.learning.ems.backend.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class NotificationServiceRequestDTO {
    private String recipient;

    private String subject;

    private String message;

    private NotificationType type;
}
