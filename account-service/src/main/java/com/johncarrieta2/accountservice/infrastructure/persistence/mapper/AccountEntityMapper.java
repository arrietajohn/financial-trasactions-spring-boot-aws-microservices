package com.johncarrieta2.accountservice.infrastructure.persistence.mapper;

import com.johncarrieta2.accountservice.domain.model.Account;
import com.johncarrieta2.accountservice.infrastructure.persistence.entity.AccountEntity;

public class AccountEntityMapper {

    public static Account toDomain(final AccountEntity entity) {
        return new Account(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getBalance()
        );
    }

    public static AccountEntity toEntity(final Account account) {
        return AccountEntity.builder()
                .id(account.getId())
                .name(account.getName())
                .email(account.getEmail())
                .balance(account.getBalance())
                .build();
    }
}
