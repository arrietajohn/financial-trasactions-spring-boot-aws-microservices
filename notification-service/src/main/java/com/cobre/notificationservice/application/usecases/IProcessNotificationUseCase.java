package com.cobre.notificationservice.application.usecases;

import com.cobre.notificationservice.application.dto.PaymentNotificationMessage;
import com.cobre.notificationservice.domain.model.Notification;

public interface IProcessNotificationUseCase
{
    void handle(Notification notification);
}
