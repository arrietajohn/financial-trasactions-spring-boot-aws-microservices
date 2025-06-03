package com.cobre.accountservice.application.mapper;

import com.cobre.accountservice.application.dto.CreateAccountCommand;
import com.cobre.accountservice.infrastructure.adapter.dto.AccountResponse;
import com.cobre.accountservice.infrastructure.adapter.dto.CreateAccountResponse;
import com.cobre.accountservice.domain.model.Account;

public class AccountMapper {

    public static Account toDomain(CreateAccountCommand request) {
        return new Account(
                request.getName(),
                request.getEmail(),
                request.getInitialBalance()
        );
    }

    public static CreateAccountResponse toResponse(Account account) {
        return new CreateAccountResponse(account.getId());
    }

    public static AccountResponse toGetResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getName(),
                account.getEmail(),
                account.getBalance()
        );
    }

}
