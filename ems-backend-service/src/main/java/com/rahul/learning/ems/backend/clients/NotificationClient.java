package com.rahul.learning.ems.backend.clients;

import com.rahul.learning.ems.backend.dtos.notifications.NotificationRequestDTO;

public interface NotificationClient {

    void send(NotificationRequestDTO notificationRequestDTO);
}