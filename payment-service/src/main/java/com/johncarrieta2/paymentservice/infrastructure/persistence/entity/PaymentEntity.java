package com.johncarrieta2.paymentservice.infrastructure.persistence.entity;

import com.johncarrieta2.paymentservice.domain.model.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID payerId;
    private UUID recipientId;
    private BigDecimal amount;
    private BigDecimal tax;
    private BigDecimal fee;
    private String reason;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private Instant timestamp;
    @Column(name = "transfer_status")
    private String transferStatus;
    @Column(name = "transfer_message", length = 1000)
    private String transferMessage;
}
