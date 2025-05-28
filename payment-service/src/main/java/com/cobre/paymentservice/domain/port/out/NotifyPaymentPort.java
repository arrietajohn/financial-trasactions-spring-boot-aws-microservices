package com.cobre.paymentservice.domain.port.out;

import java.util.UUID;

public interface NotifyPaymentPort {
    void notify(UUID paymentId);
}