package com.johncarrieta2.accountservice.application.mapper;

import com.johncarrieta2.accountservice.application.dto.CreateAccountCommand;
import com.johncarrieta2.accountservice.infrastructure.adapter.dto.AccountResponse;
import com.johncarrieta2.accountservice.infrastructure.adapter.dto.CreateAccountResponse;
import com.johncarrieta2.accountservice.domain.model.Account;

public class AccountMapper {

    public static Account toDomain(final CreateAccountCommand request) {
        return new Account(
                request.getName(),
                request.getEmail(),
                request.getInitialBalance()
        );
    }

    public static CreateAccountResponse toResponse(final Account account) {
        return new CreateAccountResponse(account.getId());
    }

    public static AccountResponse toGetResponse(final Account account) {
        return new AccountResponse(
                account.getId(),
                account.getName(),
                account.getEmail(),
                account.getBalance()
        );
    }
}
