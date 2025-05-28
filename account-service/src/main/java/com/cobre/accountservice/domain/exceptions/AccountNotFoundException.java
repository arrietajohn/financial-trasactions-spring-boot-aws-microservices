package com.cobre.accountservice.domain.exceptions;

import com.cobre.accountservice.domain.model.ErrorCodeEnum;

public class AccountNotFoundException extends DomainException {
    public AccountNotFoundException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return ErrorCodeEnum.FAILED_ACCOUNT_NOT_FOUND.getCode();
    }
}
