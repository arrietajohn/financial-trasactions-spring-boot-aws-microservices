package com.cobre.notificationservice.infrastructure.persistence.mapper;

import com.cobre.notificationservice.domain.model.Notification;
import com.cobre.notificationservice.domain.model.PaymentStatus;
import com.cobre.notificationservice.infrastructure.persistence.entity.NotificationEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class NotificationDynamoMapper {

    public NotificationEntity toEntity(Notification notification) {
        return NotificationEntity.builder()
                .id(Optional.ofNullable(notification.getId()).orElse(UUID.randomUUID()))
                .paymentId(notification.getPaymentId())
                .userId(notification.getUserId())
                .amount(notification.getAmount())
                .status(notification.getStatus())
                .reason(notification.getReason())
                .message(notification.getMessage())
                .timestamp(notification.getTimestamp())
                .build();
    }

    public Notification toDomain(NotificationEntity entity) {
        return Notification.builder()
                .id(entity.getId())
                .paymentId(entity.getPaymentId())
                .userId(entity.getUserId())
                .amount(entity.getAmount())
                .status(entity.getStatus())
                .reason(entity.getReason())
                .message(entity.getMessage())
                .timestamp(entity.getTimestamp())
                .build();
    }
}
