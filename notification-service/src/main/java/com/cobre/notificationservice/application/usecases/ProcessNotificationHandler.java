package com.cobre.notificationservice.application.usecases;

import com.cobre.notificationservice.application.mapper.INotificationMapper;
import com.cobre.notificationservice.domain.model.Notification;
import com.cobre.notificationservice.domain.port.out.INotificationRepository;
import com.cobre.notificationservice.domain.port.out.INotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessNotificationHandler implements IProcessNotificationUseCase {

    private final INotificationMapper mapper;
    private final INotificationRepository repository;
    private final INotificationSender sender;

    @Override
    public void handle(Notification notification) {
        sender.send(notification);
        repository.save(notification);

        log.info("Notification processed for paymentId={}, userId={}",
                notification.getPaymentId(), notification.getUserId());
    }

}
