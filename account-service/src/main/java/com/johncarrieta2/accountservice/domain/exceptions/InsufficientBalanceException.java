package com.johncarrieta2.accountservice.domain.exceptions;

import com.johncarrieta2.accountservice.domain.model.ErrorCodeEnum;

public class InsufficientBalanceException extends DomainException {
    public InsufficientBalanceException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return ErrorCodeEnum.FAILED_INSUFFICIENT_BALANCE.getCode();
    }
}
