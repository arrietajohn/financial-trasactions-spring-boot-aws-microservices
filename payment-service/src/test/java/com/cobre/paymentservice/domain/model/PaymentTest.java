package com.cobre.paymentservice.domain.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {

    @Test
    void shouldCreateValidPaymentWithDefaults() {
        UUID payerId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("10000.00");
        BigDecimal tax = new BigDecimal("300.00");
        BigDecimal fee = new BigDecimal("500.00");

        Payment payment = new Payment(null, payerId, recipientId, amount, tax, fee, "Pago prueba", null, null, null, null);

        assertNotNull(payment.getPaymentId());
        assertEquals(payerId, payment.getPayerId());
        assertEquals(recipientId, payment.getRecipientId());
        assertEquals(new BigDecimal("10800.00"), payment.getTotalAmount());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
        assertNotNull(payment.getTimestamp());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsInvalid() {
        UUID payerId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.ZERO;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Payment(null, payerId, recipientId, amount, null, null, "Pago inválido", null, null, null, null);
        });

        assertTrue(exception.getMessage().contains("greater than zero"));
    }

    @Test
    void shouldThrowExceptionWhenPayerIsNull() {
        UUID recipientId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("10000.00");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Payment(null, null, recipientId, amount, null, null, "Pago inválido", null, null, null, null);
        });

        assertTrue(exception.getMessage().contains("cannot be null"));
    }
}
