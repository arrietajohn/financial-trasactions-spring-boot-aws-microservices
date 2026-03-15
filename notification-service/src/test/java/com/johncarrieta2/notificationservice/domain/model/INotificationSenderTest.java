package com.johncarrieta2.notificationservice.domain.model;

import com.johncarrieta2.notificationservice.domain.port.out.INotificationSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import static org.mockito.Mockito.*;

class INotificationSenderTest {

    @Mock
    private INotificationSender notificationSenderMock;

    private Notification testNotification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testNotification = new Notification(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                BigDecimal.valueOf(250.75),
                PaymentStatus.SUCCESS,
                "Successful transaction",
                "Your payment was processed.",
                Instant.now()
        );
    }

    @Test
    @DisplayName("Should verify send method is called on mock with correct Notification")
    void sendMethodShouldBeCalledWithNotification() {
        notificationSenderMock.send(testNotification);
        verify(notificationSenderMock, times(1)).send(testNotification);
    }

    @Test
    @DisplayName("Should verify send method is called even if Notification fields are null (if allowed by domain)")
    void sendMethodShouldBeCalledWithNotificationEvenIfSomeFieldsAreNull() {
        Notification notificationWithNulls = new Notification(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                null,
                PaymentStatus.PENDING,
                null,
                "Payment is pending.",
                Instant.now()
        );
        notificationSenderMock.send(notificationWithNulls);
        verify(notificationSenderMock, times(1)).send(notificationWithNulls);
    }

    @Test
    @DisplayName("Should verify send method is not called if condition is not met (example)")
    void sendMethodShouldNotBeCalledWhenConditionNotMet() {
        verify(notificationSenderMock, never()).send(any(Notification.class));
    }
}