package com.cobre.paymentservice.infrastructure.adapter.controller;

import com.cobre.paymentservice.application.dto.ProcessPaymentCommand;
import com.cobre.paymentservice.application.dto.ProcessPaymentResponse;
import com.cobre.paymentservice.application.port.in.payment.IProcessPaymentUseCase;
import com.cobre.paymentservice.domain.model.PaymentStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final IProcessPaymentUseCase processPaymentUseCase;

    @PostMapping
    public ResponseEntity<ProcessPaymentResponse> processPayment(
            @Valid @RequestBody ProcessPaymentCommand command) {

        ProcessPaymentResponse response = processPaymentUseCase.handle(command);

        return ResponseEntity
                .status(response.getStatus() == PaymentStatus.FAILED ? 400 : 200)
                .body(response);

    }
}
