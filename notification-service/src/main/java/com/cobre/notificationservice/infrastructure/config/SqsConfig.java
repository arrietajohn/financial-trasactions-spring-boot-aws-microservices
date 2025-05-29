package com.cobre.notificationservice.infrastructure.config;

import com.cobre.notificationservice.infrastructure.config.properties.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class SqsConfig {

    private final AwsProperties aws;

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .endpointOverride(URI.create(aws.getSqs().getEndpoint()))
                .region(Region.of(aws.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                aws.getCredentials().getAccessKey(),
                                aws.getCredentials().getSecretKey())))
                .httpClientBuilder(UrlConnectionHttpClient.builder())
                .build();
    }
}
