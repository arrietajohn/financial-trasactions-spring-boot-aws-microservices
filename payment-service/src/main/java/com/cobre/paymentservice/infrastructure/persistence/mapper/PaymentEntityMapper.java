package com.johncarrieta2.paymentservice.infrastructure.persistence.mapper;

import com.johncarrieta2.paymentservice.domain.model.Payment;
import com.johncarrieta2.paymentservice.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentEntityMapper {

    public PaymentEntity toEntity(Payment payment) {
        PaymentEntity entity = new PaymentEntity();
        entity.setId(payment.getPaymentId());
        entity.setPayerId(payment.getPayerId());
        entity.setRecipientId(payment.getRecipientId());
        entity.setAmount(payment.getAmount());
        entity.setTax(payment.getTax());
        entity.setFee(payment.getFee());
        entity.setReason(payment.getReason());
        entity.setStatus(payment.getStatus());
        entity.setTimestamp(payment.getTimestamp());
        entity.setTransferStatus(payment.getTransferStatus());
        entity.setTransferMessage(payment.getTransferMessage());
        return entity;
    }

    public Payment toDomain(PaymentEntity entity) {
        return new Payment(
                entity.getId(),
                entity.getPayerId(),
                entity.getRecipientId(),
                entity.getAmount(),
                entity.getTax(),
                entity.getFee(),
                entity.getReason(),
                entity.getStatus(),
                entity.getTimestamp(),
                entity.getTransferStatus(),
                entity.getTransferMessage()

        );
    }
}
