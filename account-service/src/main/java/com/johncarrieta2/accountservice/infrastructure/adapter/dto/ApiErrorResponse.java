package com.johncarrieta2.accountservice.infrastructure.adapter.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
@Getter
@Builder
@Schema(description = "Standard error response format")
public class ApiErrorResponse {

    @Schema(example = "2025-05-28T04:24:58.369749900Z")
    private Instant timestamp;

    @Schema(example = "400")
    private int status;

    @Schema(example = "Bad Request")
    private String error;

    @Schema(example = "Insufficient balance.")
    private String message;

    @Schema(example = "FAILED_INSUFFICIENT_BALANCE")
    private String code;

    @Schema(example = "/api/v1/transfers")
    private String path;

    @Schema(example = "POST")
    private String method;

    @Schema(example = "4add184c-f975-4eb2-aa18-69237e5e905e")
    private String traceId;

    @Schema(example = "account-service")
    private String service;

    private Object details; // opcional

    // Getters y setters (o @Getter/@Setter si usas Lombok)
}
