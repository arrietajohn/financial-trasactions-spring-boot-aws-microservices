package com.cobre.accountservice.domain.model;


public enum ErrorCodeEnum {

    VALIDATION_ERROR("VALIDATION_ERROR"),
    MISSING_PARAMETER("MISSING_PARAMETER"),
    INVALID_JSON("INVALID_JSON"),
    BUSINESS_ERROR("BUSINESS_ERROR"),
    FAILED_ACCOUNT_NOT_FOUND("FAILED_ACCOUNT_NOT_FOUND"),
    FAILED_TRANSFER("FAILED_TRANSFER"),
    FAILED_INSUFFICIENT_BALANCE("FAILED_INSUFFICIENT_BALANCE"),
    FAILED_INVALID_AMOUNT("FAILED_INVALID_AMOUNT"),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND"),
    SYSTEM_ERROR("SYSTEM_ERROR");

    private final String code;

    ErrorCodeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
