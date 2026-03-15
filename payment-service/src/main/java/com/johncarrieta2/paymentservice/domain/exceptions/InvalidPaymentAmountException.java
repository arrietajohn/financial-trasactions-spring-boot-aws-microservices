package com.johncarrieta2.paymentservice.domain.exceptions;

public class InvalidPaymentAmountException extends RuntimeException {
    public InvalidPaymentAmountException(String message) {
        super(message);
    }
}