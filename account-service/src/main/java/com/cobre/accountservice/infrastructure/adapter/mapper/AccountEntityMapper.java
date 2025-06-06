package com.cobre.accountservice.infrastructure.adapter.mapper;

import com.cobre.accountservice.domain.model.Account;
import com.cobre.accountservice.infrastructure.persistence.entity.AccountEntity;

public class AccountEntityMapper {

    public static Account toDomain(AccountEntity entity) {
        return new Account(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getBalance(),
                entity.getStatus()
        );
    }

    public static AccountEntity toEntity(Account account) {
        return AccountEntity.builder()
                .id(account.getId())
                .name(account.getName())
                .email(account.getEmail())
                .balance(account.getBalance())
                .status(account.getStatus())
                .build();
    }
}
