package com.johncarrieta2.notificationservice.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    private UUID id;
    private UUID paymentId;
    private UUID userId;
    private BigDecimal amount;
    private PaymentStatus status;
    private String reason;
    private String message;
    private Instant timestamp;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        paymentId = UUID.randomUUID();
        userId = UUID.randomUUID();
        amount = BigDecimal.valueOf(100.00);
        status = PaymentStatus.SUCCESS;
        reason = "Payment successful";
        message = "Your payment has been processed successfully.";
        timestamp = Instant.now();
    }

    @Test
    @DisplayName("Should create Notification object with valid arguments")
    void shouldCreateNotificationWithValidArguments() {
        Notification notification = new Notification(id, paymentId, userId, amount, status, reason, message, timestamp);
        assertNotNull(notification);
        assertEquals(id, notification.getId());
        assertEquals(paymentId, notification.getPaymentId());
        assertEquals(userId, notification.getUserId());
        assertEquals(amount, notification.getAmount());
        assertEquals(status, notification.getStatus());
        assertEquals(reason, notification.getReason());
        assertEquals(message, notification.getMessage());
        assertEquals(timestamp, notification.getTimestamp());
    }

    @Test
    @DisplayName("Should create Notification object with null reason and amount (if allowed by business logic)")
    void shouldCreateNotificationWithNullReasonAndAmount() {
        Notification notification = new Notification(id, paymentId, userId, null, status, null, message, timestamp);
        assertNotNull(notification);
        assertNull(notification.getAmount());
        assertNull(notification.getReason());
        assertEquals(paymentId, notification.getPaymentId()); // Verify other fields are still set correctly
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if paymentId is null")
    void shouldThrowExceptionIfPaymentIdIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Notification(id, null, userId, amount, status, reason, message, timestamp));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if userId is null")
    void shouldThrowExceptionIfUserIdIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Notification(id, paymentId, null, amount, status, reason, message, timestamp));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if status is null")
    void shouldThrowExceptionIfStatusIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Notification(id, paymentId, userId, amount, null, reason, message, timestamp));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if message is null")
    void shouldThrowExceptionIfMessageIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Notification(id, paymentId, userId, amount, status, reason, null, timestamp));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException if timestamp is null")
    void shouldThrowExceptionIfTimestampIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                new Notification(id, paymentId, userId, amount, status, reason, message, null));
    }
}