package com.cobre.notificationservice.infrastructure.adapter.in.queue;

import com.cobre.notificationservice.application.dto.PaymentNotificationMessage;
import com.cobre.notificationservice.application.mapper.INotificationMapper;
import com.cobre.notificationservice.application.usecases.IProcessNotificationUseCase;
import com.cobre.notificationservice.domain.model.Notification;

import org.springframework.stereotype.Component;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.handler.annotation.Header;

@Slf4j
@Component
public class SqsNotificationListener {
    private final INotificationMapper mapper;
    private final IProcessNotificationUseCase useCase;

    public SqsNotificationListener(INotificationMapper mapper, IProcessNotificationUseCase useCase) {
        this.mapper = mapper;
        this.useCase = useCase;
    }

    @SqsListener(value = "payment-notifications")
    public void onMessage(PaymentNotificationMessage message) {
        try {
            log.info("Message received from queue by  | message={}", message);
            Notification notification = mapper.toDomain(message);
            useCase.handle(notification);
        } catch (Exception e) {
            log.error("Error processing SQS message from  | message={} | error={}", message, e.getMessage(), e);
        }
    }
}
