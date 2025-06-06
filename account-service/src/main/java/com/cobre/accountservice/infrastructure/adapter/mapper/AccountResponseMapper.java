package com.cobre.accountservice.infrastructure.adapter.mapper;

import com.cobre.accountservice.domain.model.Account;
import com.cobre.accountservice.infrastructure.adapter.dto.AccountResponse;
import com.cobre.accountservice.infrastructure.adapter.dto.UpdateAccountResponse;

public class AccountResponseMapper {
    public static AccountResponse toGetResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getName(),
                account.getEmail(),
                account.getBalance(),
                account.getStatus()
        );
    }
    public static UpdateAccountResponse toUpdateResponse(Account account, String message) {
        return   UpdateAccountResponse.builder()
                .accountId(account.getId())
                .status(account.getStatus())
                .message(message)
                .build();
    }
}
