package com.cobre.paymentservice.domain.port.out;

import com.cobre.paymentservice.domain.model.Payment;

public interface SavePaymentPort {
    void save(Payment payment);
}
