package com.johncarrieta2.paymentservice.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentNotification(
        UUID paymentId,
        UUID payerId,
        UUID recipientId,
        BigDecimal amount,
        PaymentStatus status,
        String reason,
        String message,
        Instant timestamp
) {}
