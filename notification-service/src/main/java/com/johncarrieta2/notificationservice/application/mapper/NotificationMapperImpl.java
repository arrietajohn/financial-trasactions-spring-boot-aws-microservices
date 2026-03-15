package com.johncarrieta2.notificationservice.application.mapper;

import com.johncarrieta2.notificationservice.application.dto.PaymentNotificationMessage;
import com.johncarrieta2.notificationservice.domain.model.Notification;
import com.johncarrieta2.notificationservice.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NotificationMapperImpl implements INotificationMapper {
    @Override
    public Notification toDomain(PaymentNotificationMessage dto) {
        var paymentDestination = ". Destino del pago:: " + dto.getRecipientId();;
        var message = dto.getMessage() != null ? dto.getMessage() +  paymentDestination : paymentDestination;
        return Notification.builder()
                .paymentId(dto.getPaymentId())
                .userId(dto.getPayerId())
                .amount(dto.getAmount())
                .status(dto.getStatus())
                .reason(dto.getReason())
                .message(message)
                .timestamp(dto.getTimestamp())
                .build();
    }

}
