package com.johncarrieta2.accountservice.domain.exceptions;

import com.johncarrieta2.accountservice.domain.model.ErrorCodeEnum;

public class InvalidTransferAmountException extends DomainException {
    public InvalidTransferAmountException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return ErrorCodeEnum.FAILED_TRANSFER.getCode();
    }
}

