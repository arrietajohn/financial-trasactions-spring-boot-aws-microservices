package com.johncarrieta2.notificationservice.infrastructure.adapter.out.repository;

import com.johncarrieta2.notificationservice.domain.model.Notification;
import com.johncarrieta2.notificationservice.domain.port.out.INotificationRepository;
import com.johncarrieta2.notificationservice.infrastructure.persistence.entity.NotificationEntity;
import com.johncarrieta2.notificationservice.infrastructure.persistence.mapper.NotificationDynamoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NotificationDynamoDbRepository implements INotificationRepository {

    private final DynamoDbEnhancedClient dynamoDbClient;
    private final NotificationDynamoMapper mapper;

    @Value("${aws.dynamodb.table-name}")
    private String tableName;

    @Override
    public void save(Notification notification) {
        NotificationEntity entity = mapper.toEntity(notification);
        DynamoDbTable<NotificationEntity> table = dynamoDbClient.table(tableName, TableSchema.fromBean(NotificationEntity.class));
        table.putItem(entity);

        log.info("Notification saved in DynamoDB for paymentId={}", notification.getPaymentId());
    }
}
