package com.johncarrieta2.paymentservice.domain.port.out;

import com.johncarrieta2.paymentservice.domain.model.Payment;

public interface ISavePayment {
    void save(Payment payment);
}
