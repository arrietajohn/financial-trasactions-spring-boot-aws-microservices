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
        log.info("AccountServiceClient initialized with endpoint: {}", this.accountServiceEndpoint);
    }

    @Override
    @Retry(name = "accountServiceRetry")
    @CircuitBreaker(name = "accountServiceCircuitBreaker", fallbackMethod = "fallbackTransfer")
    public TransferResult transfer(UUID payerId, UUID recipientId, BigDecimal amountToWithdraw, BigDecimal amountToTransfer) {
        log.info("AccountServiceClient - Initiating transfer: payerId={}, recipientId={}, amountToWithdraw={}, amountToTransfer={}",
                payerId, recipientId, amountToWithdraw, amountToTransfer);

        TransferRequest request = new TransferRequest(payerId, recipientId, amountToWithdraw, amountToTransfer);
        log.debug("AccountServiceClient - TransferRequest built: {}", request);

        try {
            log.info("AccountServiceClient - Sending POST request to {}", accountServiceEndpoint);
            ResponseEntity<TransferResult> response = restTemplate.postForEntity(accountServiceEndpoint, request, TransferResult.class);

            log.info("AccountServiceClient - Response received: {}", response);
            if (response == null || response.getBody() == null) {
                log.warn("AccountServiceClient - Empty or null response from account-service");
                return buildErrorResult(payerId, TransferErrorCode.EMPTY_RESPONSE, 500);
            }

            log.info("AccountServiceClient - Transfer successful, result: {}", response.getBody());
            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException ex) {
            log.warn("AccountServiceClient - HTTP error during transfer: {} [{}]", ex.getResponseBodyAsString(), ex.getStatusCode().value());
            TransferResult errorResult = parseErrorResponse(ex.getResponseBodyAsString(), ex.getStatusCode().value());
            log.debug("AccountServiceClient - Parsed error result: {}", errorResult);
            return errorResult;

        } catch (Exception ex) {
            log.error("AccountServiceClient - Unexpected error during transfer", ex);
            TransferResult errorResult = buildErrorResult(payerId, TransferErrorCode.FAILED_INTERNAL_ERROR, 500);
            log.debug("AccountServiceClient - Built internal error result: {}", errorResult);
            return errorResult;
        }
    }

    public TransferResult fallbackTransfer(UUID payerId, UUID recipientId,
                                           BigDecimal amountToWithdraw, BigDecimal amountToTransfer,
                                           Throwable throwable) {
        log.error("AccountServiceClient - Fallback triggered for transfer: payerId={}, recipientId={}, amountToWithdraw={}, amountToTransfer={}. Cause: {}",
                payerId, recipientId, amountToWithdraw, amountToTransfer, throwable.getMessage(), throwable);
        TransferResult fallbackResult = buildErrorResult(payerId, TransferErrorCode.FAILED_FALLBACK, 503);
        log.debug("AccountServiceClient - Built fallback error result: {}", fallbackResult);
        return fallbackResult;
    }

    private TransferResult parseErrorResponse(String json, int statusCode) {
        log.debug("AccountServiceClient - Attempting to parse error response: {}", json);
        try {
            TransferResult result = objectMapper.readValue(json, TransferResult.class);
            log.info("AccountServiceClient - Error response parsed successfully: {}", result);
            return result != null ? result : buildErrorResult(null, TransferErrorCode.FAILED_UNKNOWN, statusCode);
        } catch (Exception ex) {
            log.error("AccountServiceClient - Failed to parse error response: {}", json, ex);
            return buildErrorResult(null, TransferErrorCode.FAILED_UNKNOWN, statusCode);
        }
    }

    private TransferResult buildErrorResult(UUID payerId, TransferErrorCode errorCode, int status) {
        log.debug("AccountServiceClient - Building error result: payerId={}, errorCode={}, status={}", payerId, errorCode, status);
        return new TransferResult(
                payerId,
                errorCode.getCode(),
                errorCode.getMessage(),
                null,                                  // traceId
                null,                                  // path
                errorCode.getCode(),
                HTTP_METHOD,
                SERVICE_NAME,
                errorCode.name(),
                null,                                  // details
                currentTimestampIso(),
                status
        );
    }

    private String currentTimestampIso() {
        String timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        log.debug("AccountServiceClient - Generated ISO timestamp: {}", timestamp);
        return timestamp;
    }
}
