package com.cobre.paymentservice.domain.model;

import com.cobre.paymentservice.domain.port.out.INotifyPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class NotifyPaymentPortTest {

    private INotifyPayment notifyPaymentPort;
    private PaymentNotification notification;

    @BeforeEach
    void setUp() {
        notifyPaymentPort = mock(INotifyPayment.class);

        notification = new PaymentNotification(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                new BigDecimal("1000"),
                PaymentStatus.SUCCESS,
                "Payment for invoice #123",
                "Transfer completed successfully",
                Instant.now()
        );
    }

    @Test
    void shouldCallNotifyOnceWithValidNotification() {
        notifyPaymentPort.notify(notification);
        verify(notifyPaymentPort, times(1)).notify(notification);
    }

    @Test
    void shouldThrowExceptionWhenNotificationFails() {
        doThrow(new RuntimeException("Notification failure"))
                .when(notifyPaymentPort).notify(notification);

        assertThrows(RuntimeException.class, () -> notifyPaymentPort.notify(notification));
        verify(notifyPaymentPort, times(1)).notify(notification);
    }
}
