package com.cobre.paymentservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TransferResult(
        UUID senderAccountId,
        String status,
        String message,
        String traceId,
        String path,
        String code,
        String method,
        String service,
        String error,
        Map<String, String> details,
        String timestamp,
        Integer httpStatus
) {
    public boolean isSuccess() {
        return "SUCCESS".equalsIgnoreCase(status);
    }
}