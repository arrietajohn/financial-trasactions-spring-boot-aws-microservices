package com.johncarrieta2.notificationservice.application.mapper;


import com.johncarrieta2.notificationservice.application.dto.PaymentNotificationMessage;
import com.johncarrieta2.notificationservice.domain.model.Notification;

public interface INotificationMapper {
    Notification toDomain(PaymentNotificationMessage dto);
}

