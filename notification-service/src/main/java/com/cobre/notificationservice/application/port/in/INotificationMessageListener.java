package com.cobre.notificationservice.application.port.in;

import com.cobre.notificationservice.application.dto.PaymentNotificationMessage;

public interface INotificationMessageListener {
    void receive(PaymentNotificationMessage message);
}
