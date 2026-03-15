package com.johncarrieta2.paymentservice.application.dto;

import com.johncarrieta2.paymentservice.domain.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentResponse {
    private UUID paymentId;
    private PaymentStatus status;
    private String message;
}
