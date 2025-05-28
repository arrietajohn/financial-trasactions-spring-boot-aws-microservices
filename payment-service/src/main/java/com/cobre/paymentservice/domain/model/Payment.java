package com.cobre.paymentservice.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Payment {

    private final UUID paymentId;
    private final UUID payerId;
    private final UUID recipientId;
    private final BigDecimal amount;
    private final BigDecimal tax;
    private final BigDecimal fee;
    private final BigDecimal totalAmount;
    private final String reason;
    private final PaymentStatus status;
    private final Instant timestamp;

    public Payment(UUID paymentId,
                   UUID payerId,
                   UUID recipientId,
                   BigDecimal amount,
                   BigDecimal tax,
                   BigDecimal fee,
                   String reason,
                   PaymentStatus status,
                   Instant timestamp) {

        if (payerId == null || recipientId == null) {
            throw new IllegalArgumentException("Payer and recipient IDs cannot be null");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        this.paymentId = paymentId != null ? paymentId : UUID.randomUUID();
        this.payerId = payerId;
        this.recipientId = recipientId;
        this.amount = amount;
        this.tax = tax != null ? tax : BigDecimal.ZERO;
        this.fee = fee != null ? fee : BigDecimal.ZERO;
        this.totalAmount = amount.add(this.tax).add(this.fee);
        this.reason = reason;
        this.status = status != null ? status : PaymentStatus.PENDING;
        this.timestamp = timestamp != null ? timestamp : Instant.now();
    }
}
