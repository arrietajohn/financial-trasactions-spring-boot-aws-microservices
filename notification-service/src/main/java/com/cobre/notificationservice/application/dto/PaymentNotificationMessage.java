package com.cobre.notificationservice.application.dto;

import com.cobre.notificationservice.domain.model.PaymentStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class PaymentNotificationMessage {

        private UUID paymentId;
        private UUID payerId;
        private UUID recipientId;
        private BigDecimal amount;
        private PaymentStatus status;
        private String reason;
        private String message;
        private Instant timestamp;
    }
