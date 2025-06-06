package com.cobre.accountservice.domain.model;

public enum TransactionTypeEnum {
    SUCCESS,
    FAILED_INSUFFICIENT_BALANCE,
    FAILED_ACCOUNT_NOT_FOUND,
    FAILED_INVALID_AMOUNT,
    FAILED_INTERNAL_ERROR
}
