package com.cobre.paymentservice.domain.model;

import com.cobre.paymentservice.domain.port.out.ISavePayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SavePaymentPortTest {

    private ISavePayment savePaymentPort;

    @BeforeEach
    void setUp() {
        savePaymentPort = mock(ISavePayment.class);
    }

    @Test
    void shouldCallSaveOnceWithValidPayment() {
        Payment payment = new Payment(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                new BigDecimal("10000.00"),
                new BigDecimal("300.00"),
                new BigDecimal("500.00"),
                "Pago de prueba",
                PaymentStatus.PENDING,
                Instant.now()
        );

        savePaymentPort.save(payment);

        verify(savePaymentPort, times(1)).save(payment);
    }

    @Test
    void shouldThrowExceptionWhenSaveFails() {
        Payment payment = new Payment(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                new BigDecimal("10000.00"),
                new BigDecimal("300.00"),
                new BigDecimal("500.00"),
                "Pago fallido",
                PaymentStatus.PENDING,
                Instant.now()
        );

        doThrow(new RuntimeException("DB error")).when(savePaymentPort).save(payment);

        assertThrows(RuntimeException.class, () -> savePaymentPort.save(payment));
        verify(savePaymentPort, times(1)).save(payment);
    }
}
