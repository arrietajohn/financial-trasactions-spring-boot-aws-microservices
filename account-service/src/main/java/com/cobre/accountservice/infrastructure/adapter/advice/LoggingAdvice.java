package com.cobre.accountservice.infrastructure.adapter.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@ControllerAdvice
public class LoggingAdvice  {

    private static final String SERVICE_NAME = "account-service";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String traceId = generateTraceId();

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        log.warn("[{}] [VALIDATION] {} {} - validation errors: {}", traceId, request.getMethod(), request.getRequestURI(), fieldErrors);

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", traceId, request, fieldErrors, "VALIDATION_ERROR");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        String traceId = generateTraceId();

        log.warn("[{}] [BUSINESS] {} {} - {}", traceId, request.getMethod(), request.getRequestURI(), ex.getMessage());

        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), traceId, request, null, "BUSINESS_ERROR");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, HttpServletRequest request) {
        String traceId = generateTraceId();

        log.error("[{}] [SYSTEM] {} {} - {}: {}", traceId, request.getMethod(), request.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage(), ex);

        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", traceId, request, null, "SYSTEM_ERROR");
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, String message, String traceId,
                                                 HttpServletRequest request, Object details, String errorCode) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("code", errorCode);
        if (details != null) body.put("details", details);
        body.put("path", request.getRequestURI());
        body.put("method", request.getMethod());
        body.put("traceId", traceId);
        body.put("service", SERVICE_NAME);

        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFound(NoHandlerFoundException ex, HttpServletRequest request) {
        String traceId = generateTraceId();

        log.warn("[{}] [NOT_FOUND] {} {} - Endpoint not found", traceId, request.getMethod(), request.getRequestURI());

        return buildResponse(HttpStatus.NOT_FOUND,
                "The requested resource " + request.getRequestURI() + " does not exist.",
                traceId,
                request,
                null,
                "RESOURCE_NOT_FOUND");
    }



    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        String traceId = generateTraceId();

        log.warn("[{}] [NOT_FOUND] {} {} - Resource not found", traceId, request.getMethod(), request.getRequestURI());

        return buildResponse(HttpStatus.NOT_FOUND,
                "The requested resource " + request.getRequestURI() + " does not exist.",
                traceId,
                request,
                null,
                "RESOURCE_NOT_FOUND");
    }


    private String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}
