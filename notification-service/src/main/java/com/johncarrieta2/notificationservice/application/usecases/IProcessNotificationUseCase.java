package com.johncarrieta2.notificationservice.application.usecases;

import com.johncarrieta2.notificationservice.application.dto.PaymentNotificationMessage;
import com.johncarrieta2.notificationservice.domain.model.Notification;

public interface IProcessNotificationUseCase
{
    void handle(Notification notification);
}
