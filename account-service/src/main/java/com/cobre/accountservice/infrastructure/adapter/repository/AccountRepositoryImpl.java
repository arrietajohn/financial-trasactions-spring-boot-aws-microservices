package com.cobre.accountservice.infrastructure.adapter.repository;

import com.cobre.accountservice.domain.exceptions.AccountNotFoundException;
import com.cobre.accountservice.domain.model.Account;
import com.cobre.accountservice.domain.port.out.IAccountRepository;
import com.cobre.accountservice.infrastructure.adapter.mapper.AccountEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements IAccountRepository {

    private final IAccountJpaRepository accountJpaRepository;

    @Override
    public void save(Account account) {
        accountJpaRepository.save(AccountEntityMapper.toEntity(account));
    }

    @Override
    public Optional<Account> findById(UUID accountId) {
        return accountJpaRepository.findById(accountId)
                .map(AccountEntityMapper::toDomain);
    }

    @Override
    public void update(Account account) {
        accountJpaRepository.save(AccountEntityMapper.toEntity(account));
    }

}
