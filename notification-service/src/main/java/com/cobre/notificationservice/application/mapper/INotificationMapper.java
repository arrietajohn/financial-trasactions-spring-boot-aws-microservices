package com.cobre.notificationservice.application.mapper;


import com.cobre.notificationservice.application.dto.PaymentNotificationMessage;
import com.cobre.notificationservice.domain.model.Notification;

public interface INotificationMapper {
    Notification toDomain(PaymentNotificationMessage dto);
}

