package com.cobre.accountservice.domain.port.out;

import com.cobre.accountservice.domain.model.Account;
import java.util.Optional;
import java.util.UUID;

public interface IAccountRepository {
    Optional<Account> findById(UUID accountId);
    void save(Account account);
    void update(Account account);

}
