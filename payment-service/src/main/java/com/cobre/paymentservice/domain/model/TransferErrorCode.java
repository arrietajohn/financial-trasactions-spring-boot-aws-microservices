package com.cobre.paymentservice.domain.model;


public enum TransferErrorCode {

    FAILED_INTERNAL_ERROR("FAILED_INTERNAL_ERROR", "Internal server error"),
    FAILED_FALLBACK("FAILED_FALLBACK", "Transfer temporarily unavailable"),
    FAILED_UNKNOWN("FAILED_UNKNOWN", "Unable to parse error message"),
    EMPTY_RESPONSE("FAILED_INTERNAL_ERROR", "Empty response from account-service");

    private final String code;
    private final String message;

    TransferErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
