package com.cobre.notificationservice.domain.port.out;

import com.cobre.notificationservice.domain.model.Notification;

public interface iNotificationRepository {
    void save(Notification notification);
}
