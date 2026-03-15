package com.johncarrieta2.paymentservice.infrastructure.adapter.external;

import com.johncarrieta2.paymentservice.domain.model.PaymentNotification;
import com.johncarrieta2.paymentservice.domain.port.out.INotifyPayment;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@Component
public class PaymentNotificationMessagePublisher implements INotifyPayment {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final String queueUrl;

    public PaymentNotificationMessagePublisher(
            SqsClient sqsClient,
            ObjectMapper objectMapper,
            @Value("${aws.sqs.payment-queue-url}") String queueUrl) {
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
        this.queueUrl = queueUrl;
        log.info("PaymentNotificationMessagePublisher initialized with queueUrl={}", this.queueUrl);
    }

    @Retry(name = "notificationServiceRetry")
    @CircuitBreaker(name = "notificationServiceCircuitBreaker", fallbackMethod = "fallbackNotify")
    @Override
    public void notify(PaymentNotification notification) {
        log.info("Starting SQS notification for Payment ID: {}", notification.paymentId());
        try {
            String message = objectMapper.writeValueAsString(notification);
            log.debug("Serialized notification for Payment ID {}: {}", notification.paymentId(), message);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(message)
                    .build();
            log.debug("Prepared SendMessageRequest: {}", request);

            sqsClient.sendMessage(request);
            log.info("Message sent to SQS for Payment ID: {}", notification.paymentId());
        } catch (Exception e) {
            log.error("Failed to send payment notification to SQS for Payment ID {}: {}", notification.paymentId(), e);
            throw new RuntimeException("Could not send message to SQS", e);
        }
    }

    public void fallbackNotify(PaymentNotification notification, Throwable ex) {
        log.warn("Fallback: Could not notify for Payment ID {}. Reason: {}", notification.paymentId(), ex.getMessage(), ex);
    }
}
