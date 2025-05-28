package com.cobre.paymentservice.application.port.in.payment;

import com.cobre.paymentservice.application.dto.ProcessPaymentCommand;
import com.cobre.paymentservice.application.dto.ProcessPaymentResponse;

public interface IProcessPaymentUseCase {
    ProcessPaymentResponse handle(ProcessPaymentCommand command);
}
