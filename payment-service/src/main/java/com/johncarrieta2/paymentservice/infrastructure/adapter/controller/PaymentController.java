package com.johncarrieta2.paymentservice.infrastructure.adapter.controller;

import com.johncarrieta2.paymentservice.application.dto.ProcessPaymentCommand;
import com.johncarrieta2.paymentservice.application.dto.ProcessPaymentResponse;
import com.johncarrieta2.paymentservice.application.port.in.payment.IProcessPaymentUseCase;
import com.johncarrieta2.paymentservice.domain.model.PaymentStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final IProcessPaymentUseCase processPaymentUseCase;

    @PostMapping
    public ResponseEntity<ProcessPaymentResponse> processPayment(
            @Valid @RequestBody ProcessPaymentCommand command) {
        log.info("Payment processed successfully. Payment ID: {}", command.getPayerId() + "-" + command.getRecipientId() + "-" + command.getAmount() + "-" + command.getReason());

        ProcessPaymentResponse response = processPaymentUseCase.handle(command);


        return ResponseEntity
                .status(response.getStatus() == PaymentStatus.FAILED ? 400 : 200)
                .body(response);

    }
}
