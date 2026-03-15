package com.johncarrieta2.paymentservice.application.port.in.payment;

import com.johncarrieta2.paymentservice.application.dto.ProcessPaymentCommand;
import com.johncarrieta2.paymentservice.application.dto.ProcessPaymentResponse;

public interface IProcessPaymentUseCase {
    ProcessPaymentResponse handle(ProcessPaymentCommand command);
}
