package com.cobre.notificationservice.infrastructure.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "aws")
public class AwsProperties {

    private String region;
    private Credentials credentials;
    private DynamoDb dynamodb;
    private Sqs sqs;

    @Getter @Setter
    public static class Credentials {
        private String accessKey;
        private String secretKey;
    }

    @Getter @Setter
    public static class DynamoDb {
        private String endpoint;
        private String tableName;
    }

    @Getter @Setter
    public static class Sqs {
        private String endpoint;
        private String queueUrl;
        private String queueName;
    }
}
