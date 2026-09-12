package com.rahul.learning.ems.notification.services.impl;

import com.rahul.learning.ems.notification.dtos.NotificationRequestDTO;
import com.rahul.learning.ems.notification.dtos.NotificationResponseDTO;
import com.rahul.learning.ems.notification.entities.Notification;
import com.rahul.learning.ems.notification.enums.NotificationStatus;
import com.rahul.learning.ems.notification.repos.NotificationRepository;
import com.rahul.learning.ems.notification.services.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public NotificationResponseDTO createNotification(NotificationRequestDTO request) {

        Notification notification = Notification.builder()
                .recipient(request.getRecipient())
                .subject(request.getSubject())
                .message(request.getMessage())
                .type(request.getType())
                .status(NotificationStatus.PENDING)
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        return mapToResponseDTO(savedNotification);
    }

    private NotificationResponseDTO mapToResponseDTO(Notification notification) {
        return new NotificationResponseDTO(
                notification.getNotificationId(),
                notification.getRecipient(),
                notification.getSubject(),
                notification.getMessage(),
                notification.getType(),
                notification.getStatus(),
                notification.getCreatedAt(),
                notification.getSentAt()
        );
    }
}