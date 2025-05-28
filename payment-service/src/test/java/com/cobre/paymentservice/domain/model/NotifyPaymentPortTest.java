package com.cobre.paymentservice.domain.model;

import com.cobre.paymentservice.domain.port.out.INotifyPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class NotifyPaymentPortTest {

    private INotifyPayment notifyPaymentPort;

    @BeforeEach
    void setUp() {
        notifyPaymentPort = mock(INotifyPayment.class);
    }

    @Test
    void shouldCallNotifyOnceWithValidPaymentId() {
        UUID paymentId = UUID.randomUUID();

        notifyPaymentPort.notify(paymentId);

        verify(notifyPaymentPort, times(1)).notify(paymentId);
    }

    @Test
    void shouldThrowExceptionWhenNotificationFails() {
        UUID paymentId = UUID.randomUUID();

        doThrow(new RuntimeException("Notification failure")).when(notifyPaymentPort).notify(paymentId);

        assertThrows(RuntimeException.class, () -> notifyPaymentPort.notify(paymentId));

        verify(notifyPaymentPort, times(1)).notify(paymentId);
    }
}
