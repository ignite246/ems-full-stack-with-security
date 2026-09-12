package com.rahul.learning.ems.notification.services;

import com.rahul.learning.ems.notification.dtos.NotificationRequestDTO;
import com.rahul.learning.ems.notification.dtos.NotificationResponseDTO;

public interface NotificationService {
    NotificationResponseDTO createNotification(NotificationRequestDTO request);
}
