package com.cobre.paymentservice.infrastructure.adapter.external;

import com.cobre.paymentservice.domain.model.PaymentNotification;
import com.cobre.paymentservice.domain.port.out.INotifyPayment;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentNotificationMessagePublisher implements INotifyPayment {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private static final String QUEUE_URL = "http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/payment-notifications";

    @Retry(name = "notificationServiceRetry")
    @CircuitBreaker(name = "notificationServiceCircuitBreaker", fallbackMethod = "fallbackNotify")
    @Override
    public void notify(PaymentNotification notification) {
        try {
            String message = objectMapper.writeValueAsString(notification);
            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(QUEUE_URL)
                    .messageBody(message)
                    .build();
            sqsClient.sendMessage(request);
            log.info("Message sent to SQS for Payment ID: {}", notification.paymentId());
        } catch (Exception e) {
            log.error("Failed to send payment notification to SQS: {}", e.getMessage(), e);
            throw new RuntimeException("Could not send message to SQS", e);
        }
    }

    public void fallbackNotify(PaymentNotification notification, Throwable ex) {
        log.warn("Fallback: Could not notify for payment ID {}. Reason: {}", notification.paymentId(), ex.getMessage());
    }
}
