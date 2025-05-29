package com.cobre.paymentservice.infrastructure.adapter.external;

import com.cobre.paymentservice.domain.model.TransferErrorCode;
import com.cobre.paymentservice.domain.model.TransferResult;
import com.cobre.paymentservice.domain.port.out.ITransferMoney;
import com.cobre.paymentservice.infrastructure.adapter.dto.TransferRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
public class AccountServiceClient implements ITransferMoney {

    private static final String HTTP_METHOD = "POST";
    private static final String SERVICE_NAME = "account-service";

    private final RestTemplate restTemplate;
    private final String accountServiceEndpoint;
    private final ObjectMapper objectMapper;

    public AccountServiceClient(RestTemplate restTemplate,
                                @Value("${clients.account-service.url}") String accountServiceEndpoint) {
        this.restTemplate = restTemplate;
        this.accountServiceEndpoint = accountServiceEndpoint;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    @Retry(name = "accountServiceRetry")
    @CircuitBreaker(name = "accountServiceCircuitBreaker", fallbackMethod = "fallbackTransfer")
    public TransferResult transfer(UUID payerId, UUID recipientId, BigDecimal amountToWithdraw, BigDecimal amountToTransfer) {
        TransferRequest request = new TransferRequest(payerId, recipientId, amountToWithdraw, amountToTransfer);

        try {
            ResponseEntity<TransferResult> response = restTemplate.postForEntity(accountServiceEndpoint, request, TransferResult.class);

            if (response == null || response.getBody() == null) {
                log.warn("AccountServiceClient - Empty or null response from account-service");
                return buildErrorResult(payerId, TransferErrorCode.EMPTY_RESPONSE, 500);
            }

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.warn("AccountServiceClient - Transfer failed: {} [{}]", ex.getResponseBodyAsString(), ex.getStatusCode().value());
            return parseErrorResponse(ex.getResponseBodyAsString(), ex.getStatusCode().value());

        } catch (Exception ex) {
            log.error("AccountServiceClient - Unexpected error during transfer", ex);
            return buildErrorResult(payerId, TransferErrorCode.FAILED_INTERNAL_ERROR, 500);
        }
    }

    public TransferResult fallbackTransfer(UUID payerId, UUID recipientId,
                                           BigDecimal amountToWithdraw, BigDecimal amountToTransfer,
                                           Throwable throwable) {
        log.error("AccountServiceClient - Fallback triggered", throwable);
        return buildErrorResult(payerId, TransferErrorCode.FAILED_FALLBACK, 503);
    }

    private TransferResult parseErrorResponse(String json, int statusCode) {
        try {
            TransferResult result = objectMapper.readValue(json, TransferResult.class);
            return result != null ? result : buildErrorResult(null, TransferErrorCode.FAILED_UNKNOWN, statusCode);
        } catch (Exception ex) {
            log.error("AccountServiceClient - Failed to parse error response: {}", json, ex);
            return buildErrorResult(null, TransferErrorCode.FAILED_UNKNOWN, statusCode);
        }
    }

    private TransferResult buildErrorResult(UUID payerId, TransferErrorCode errorCode, int status) {
        return new TransferResult(
                payerId,
                errorCode.getCode(),
                errorCode.getMessage(),
                null,                                  // traceId
                null,                                         // path
                errorCode.getCode(),
                HTTP_METHOD,
                SERVICE_NAME,
                errorCode.name(),
                null,                                   // detaills
                currentTimestampIso(),
                status
        );
    }
    private String currentTimestampIso() {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.now());
    }


}
