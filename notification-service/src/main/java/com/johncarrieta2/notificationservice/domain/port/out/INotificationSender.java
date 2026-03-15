package com.johncarrieta2.notificationservice.domain.port.out;

import com.johncarrieta2.notificationservice.domain.model.Notification;

public interface INotificationSender {
    void send(Notification notification);
}
