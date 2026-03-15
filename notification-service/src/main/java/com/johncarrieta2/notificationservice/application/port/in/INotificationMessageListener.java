package com.johncarrieta2.notificationservice.application.port.in;

import com.johncarrieta2.notificationservice.application.dto.PaymentNotificationMessage;

public interface INotificationMessageListener {
    void receive(PaymentNotificationMessage message);
}
