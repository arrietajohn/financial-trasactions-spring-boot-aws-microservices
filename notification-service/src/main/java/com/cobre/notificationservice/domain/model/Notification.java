package com.cobre.notificationservice.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class Notification {

    private final UUID id;
    private final UUID paymentId;
    private final UUID userId;
    private final BigDecimal amount;
    private final PaymentStatus status;
    private final String reason;
    private final String message;
    private final Instant timestamp;

    public Notification(UUID id,
                        UUID paymentId,
                        UUID userId,
                        BigDecimal amount,
                        PaymentStatus status,
                        String reason,
                        String message,
                        Instant timestamp) {

        if (paymentId == null || userId == null || status == null
                || message == null || timestamp == null) {
            throw new IllegalArgumentException("Notification fields cannot be null.");
        }

        this.id = id;
        this.paymentId = paymentId;
        this.userId = userId;
        this.amount = amount;
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }
}
