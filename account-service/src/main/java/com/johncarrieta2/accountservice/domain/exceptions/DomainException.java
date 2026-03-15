package com.johncarrieta2.accountservice.domain.exceptions;

public abstract class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }

    public abstract String getCode();
}
