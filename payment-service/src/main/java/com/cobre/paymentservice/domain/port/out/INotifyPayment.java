package com.cobre.paymentservice.domain.port.out;

import java.util.UUID;

public interface INotifyPayment {
    void notify(UUID paymentId);
}