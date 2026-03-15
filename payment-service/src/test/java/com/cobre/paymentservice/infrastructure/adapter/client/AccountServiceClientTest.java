package com.johncarrieta2.paymentservice.infrastructure.adapter.client;

import com.johncarrieta2.paymentservice.domain.model.TransferErrorCode;
import com.johncarrieta2.paymentservice.domain.model.TransferResult;
import com.johncarrieta2.paymentservice.infrastructure.adapter.external.AccountServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceClientTest {

    @Mock
    private RestTemplate restTemplate;

    private AccountServiceClient accountServiceClient;

    private final UUID payerId = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11");
    private final UUID recipientId = UUID.fromString("b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22");
    private final BigDecimal amountToWithdraw = new BigDecimal("100.00");
    private final BigDecimal amountToTransfer = new BigDecimal("99.50");
    private final String accountServiceUrl = "http://localhost:8081/api/accounts/transfer";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        accountServiceClient = new AccountServiceClient(restTemplate, accountServiceUrl);
    }

    @Test
    @DisplayName("Transferencia exitosa")
    void transfer_successful() {
        TransferResult mockResponse = new TransferResult(
                payerId,
                "SUCCESS",
                "Transfer completed",
                "trace123",
                "/api/transfer",
                "200_OK",
                "POST",
                "account-service",
                null,
                Map.of(),
                "2025-05-29T03:00:00Z",
                200
        );

        when(restTemplate.postForEntity(anyString(), any(), eq(TransferResult.class)))
                .thenReturn(ResponseEntity.ok(mockResponse));

        TransferResult result = accountServiceClient.transfer(payerId, recipientId, amountToWithdraw, amountToTransfer);

        assertNotNull(result);
        assertEquals("SUCCESS", result.status());
        assertTrue(result.isSuccess());
    }

    @Test
    @DisplayName("Fallback por excepción inesperada")
    void fallbackTransfer_invoked() {
        when(restTemplate.postForEntity(anyString(), any(), eq(TransferResult.class)))
                .thenThrow(new RuntimeException("Simulated failure"));

        TransferResult result = accountServiceClient.transfer(payerId, recipientId, amountToWithdraw, amountToTransfer);

        assertNotNull(result);
        assertEquals(TransferErrorCode.FAILED_INTERNAL_ERROR.getCode(), result.code());
        assertEquals(500, result.httpStatus());
        assertFalse(result.isSuccess());
    }

    @Test
    @DisplayName("Error 400 (HttpClientErrorException)")
    void transfer_httpClientError() {
        TransferResult errorResult = new TransferResult(
                payerId,
                "FAILED_UNKNOWN",
                "Invalid amount",
                "trace123",
                "/api/transfer",
                "FAILED_UNKNOWN",
                "POST",
                "account-service",
                "FAILED_UNKNOWN",
                Map.of("amount", "must be positive"),
                "2025-05-29T03:00:00Z",
                400
        );

        String errorJson;
        try {
            errorJson = objectMapper.writeValueAsString(errorResult);
        } catch (Exception e) {
            throw new RuntimeException("JSON build failed");
        }

        HttpClientErrorException exception = new HttpClientErrorException(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                errorJson.getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );

        when(restTemplate.postForEntity(anyString(), any(), eq(TransferResult.class)))
                .thenThrow(exception);

        TransferResult result = accountServiceClient.transfer(payerId, recipientId, amountToWithdraw, amountToTransfer);

        assertEquals("FAILED_UNKNOWN", result.code());
        assertEquals(400, result.httpStatus());
    }

    @Test
    @DisplayName("Error 500 (HttpServerErrorException)")
    void transfer_httpServerError() {
        HttpServerErrorException exception = new HttpServerErrorException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "{ \"invalid\": \"json\" ".getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );

        when(restTemplate.postForEntity(anyString(), any(), eq(TransferResult.class)))
                .thenThrow(exception);

        TransferResult result = accountServiceClient.transfer(payerId, recipientId, amountToWithdraw, amountToTransfer);

        assertEquals(TransferErrorCode.FAILED_UNKNOWN.getCode(), result.code());
        assertEquals(500, result.httpStatus());
    }

    @Test
    @DisplayName("Respuesta nula o vacía")
    void transfer_emptyResponse() {
        when(restTemplate.postForEntity(anyString(), any(), eq(TransferResult.class)))
                .thenReturn(null); // simulamos respuesta nula

        TransferResult result = accountServiceClient.transfer(payerId, recipientId, amountToWithdraw, amountToTransfer);

        assertEquals(TransferErrorCode.EMPTY_RESPONSE.getCode(), result.code());
        assertEquals(500, result.httpStatus());
    }
}
