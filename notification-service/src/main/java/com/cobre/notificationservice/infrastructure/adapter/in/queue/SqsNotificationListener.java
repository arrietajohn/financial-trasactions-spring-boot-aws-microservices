package com.cobre.notificationservice.infrastructure.adapter.in.queue;

import com.cobre.notificationservice.application.dto.PaymentNotificationMessage;
import com.cobre.notificationservice.application.mapper.INotificationMapper;
import com.cobre.notificationservice.application.usecases.IProcessNotificationUseCase;
import com.cobre.notificationservice.domain.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Header;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsNotificationListener {

    private final INotificationMapper mapper;
    private final IProcessNotificationUseCase useCase;

    @SqsListener(value = "payment-notifications")
    public void onMessage(PaymentNotificationMessage message,
                          @Header("SenderId") String senderId) {

        log.info("Message received from queue by senderId={} | paymentId={}", senderId, message.getPaymentId());

        Notification notification = mapper.toDomain(message);

        useCase.handle(notification);
    }
}
