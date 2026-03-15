package com.johncarrieta2.paymentservice.domain.port.out;

import com.johncarrieta2.paymentservice.domain.model.PaymentNotification;

import java.util.UUID;

public interface INotifyPayment {
    void notify(PaymentNotification notification);
}