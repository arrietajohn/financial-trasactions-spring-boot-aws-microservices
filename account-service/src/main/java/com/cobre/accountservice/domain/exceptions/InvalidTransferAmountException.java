package com.cobre.accountservice.domain.exceptions;

public class InvalidTransferAmountException extends  RuntimeException {
    public InvalidTransferAmountException(String message) {
        super(message);
    }
}
