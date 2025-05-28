package com.cobre.accountservice.domain.exceptions;

import com.cobre.accountservice.domain.model.ErrorCodeEnum;

public class InvalidAmountException extends DomainException {
    public InvalidAmountException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return ErrorCodeEnum.FAILED_INVALID_AMOUNT.getCode();
    }
}
