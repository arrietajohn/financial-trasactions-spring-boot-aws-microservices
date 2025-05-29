package com.cobre.paymentservice.domain.port.out;

import com.cobre.paymentservice.domain.model.PaymentNotification;

import java.util.UUID;

public interface INotifyPayment {
    void notify(PaymentNotification notification);
}