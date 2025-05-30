package com.cobre.notificationservice.infrastructure.adapter.out.email;

import com.cobre.notificationservice.domain.model.Notification;
import com.cobre.notificationservice.domain.port.out.INotificationSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailNotificationSender implements INotificationSender {

    @Override
    public void send(Notification notification) {
        log.info("📧 Sending notification to user {} about payment {}",
                notification.getUserId(), notification.getPaymentId());
    }
}
