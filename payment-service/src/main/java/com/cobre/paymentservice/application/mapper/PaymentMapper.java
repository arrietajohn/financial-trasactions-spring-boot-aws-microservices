package com.cobre.paymentservice.application.mapper;

import com.cobre.paymentservice.application.dto.ProcessPaymentCommand;
import com.cobre.paymentservice.application.dto.ProcessPaymentResponse;
import com.cobre.paymentservice.domain.model.Payment;
import com.cobre.paymentservice.domain.model.PaymentNotification;
import com.cobre.paymentservice.domain.model.PaymentStatus;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.Instant;

@Component
public class PaymentMapper {

    public Payment toDomain(ProcessPaymentCommand cmd, BigDecimal tax, BigDecimal fee) {
        return new Payment(
                null,                        // payment id
                cmd.getPayerId(),
                cmd.getRecipientId(),
                cmd.getAmount(),
                tax,
                fee,
                cmd.getReason(),
                PaymentStatus.PENDING,
                null,                       // timestamp
                null,                       // transfer status
                null                        // transfer message
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
                base.getTimestamp() != null ? base.getTimestamp() : Instant.now(),
                base.getTransferStatus(),
                base.getTransferMessage()
        );
    }

    public ProcessPaymentResponse toResponse(Payment payment) {
        String msg = payment.getTransferMessage() != null
                ? payment.getTransferMessage()
                : (payment.getStatus() == PaymentStatus.SUCCESS ? "Payment completed" : "Payment failed");

        return new ProcessPaymentResponse(payment.getPaymentId(), payment.getStatus(), msg);
    }

    public PaymentNotification toNotification(Payment payment, String message) {
        return new PaymentNotification(
                payment.getPaymentId(),
                payment.getPayerId(),
                payment.getRecipientId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getReason(),
                message,
                payment.getTimestamp()
        );
    }

    public Payment withStatus(Payment payment, PaymentStatus status, String transferStatus, String transferMessage) {
        return new Payment(
                payment.getPaymentId(),
                payment.getPayerId(),
                payment.getRecipientId(),
                payment.getAmount(),
                payment.getTax(),
                payment.getFee(),
                payment.getReason(),
                status,
                payment.getTimestamp() != null ? payment.getTimestamp() : Instant.now(),
                transferStatus,
                transferMessage
        );
    }
}
