package com.johncarrieta2.paymentservice.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentCommand {
    @NotNull(message = "Payer ID must not be null")
    private UUID payerId;

    @NotNull(message = "Recipient ID must not be null")
    private UUID recipientId;

    @NotNull(message = "Amount must not be null")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    private String reason;
}