package com.cobre.accountservice.infrastructure.adapter.repository;

import com.cobre.accountservice.domain.exceptions.AccountNotFoundException;
import com.cobre.accountservice.domain.model.Account;
import com.cobre.accountservice.domain.port.out.IAccountRepository;
import com.cobre.accountservice.infrastructure.persistence.mapper.AccountEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements IAccountRepository {

    private final IAccountJpaRepository accountJpaRepository;

    @Override
    public void save(final Account account) {
        accountJpaRepository.save(AccountEntityMapper.toEntity(account));
    }

    @Override
    public Optional<Account> findById(final UUID accountId) {
        return accountJpaRepository.findById(accountId)
                .map(AccountEntityMapper::toDomain);
    }

    @Override
    public void update(final Account account) {
        final UUID id = account.getId();
        if (!accountJpaRepository.existsById(id)) {
            throw new AccountNotFoundException("Cannot update non-existent account with ID: " + id);
        }
        accountJpaRepository.save(AccountEntityMapper.toEntity(account));
    }
}
