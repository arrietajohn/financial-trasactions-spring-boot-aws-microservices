package com.cobre.paymentservice.application.mapper;

import com.cobre.paymentservice.application.dto.ProcessPaymentCommand;
import com.cobre.paymentservice.application.dto.ProcessPaymentResponse;
import com.cobre.paymentservice.domain.model.Payment;
import com.cobre.paymentservice.domain.model.PaymentStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class PaymentMapper {

    public Payment toDomain(ProcessPaymentCommand cmd, BigDecimal tax, BigDecimal fee) {
        return new Payment(
                null,
                cmd.getPayerId(),
                cmd.getRecipientId(),
                cmd.getAmount(),
                tax,
                fee,
                cmd.getReason(),
                PaymentStatus.PENDING,
                null
        );
    }

    public Payment withStatus(Payment base, PaymentStatus status) {
        return new Payment(
                base.getPaymentId(),
                base.getPayerId(),
                base.getRecipientId(),
                base.getAmount(),
                base.getTax(),
                base.getFee(),
                base.getReason(),
                status,
                base.getTimestamp() != null ? base.getTimestamp() : Instant.now()
        );
    }

    public ProcessPaymentResponse toResponse(Payment payment) {
        String msg = payment.getStatus() == PaymentStatus.SUCCESS ? "Payment completed" : "Payment failed";
        return new ProcessPaymentResponse(payment.getPaymentId(), payment.getStatus(), msg);
    }
}
