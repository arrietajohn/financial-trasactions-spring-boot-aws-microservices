package com.johncarrieta2.paymentservice.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
public class SqsConfig {
    @Value("${aws.sqs.payment-queue-url}")
    private String urlQueue;

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .endpointOverride(URI.create(urlQueue)) // LocalStack endpoint
                .region(Region.US_EAST_1)
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create("test", "test")
                        )
                )
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .build();
    }
}
