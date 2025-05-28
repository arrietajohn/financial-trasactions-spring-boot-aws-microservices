package com.cobre.accountservice.infrastructure.adapter.repository;

import com.cobre.accountservice.domain.model.Account;
import com.cobre.accountservice.domain.port.out.IAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class AccountRepositoryTest {

    private IAccountRepository accountRepository;

    @BeforeEach
    void setUp() {

        accountRepository = new IAccountRepository() {
            private Account storedAccount;

            @Override
            public Optional<Account> findById(UUID accountId) {
                return Optional.ofNullable(storedAccount)
                        .filter(account -> account.getId().equals(accountId));
            }

            @Override
            public void save(Account account) {
                this.storedAccount = account;
            }

            @Override
            public void update(Account account) {

            }

        };
    }

    @Test
    void saveAndFindById_ShouldWorkCorrectly() {
        Account account = new Account("Alice", "alice@mail.com", BigDecimal.valueOf(100));
        accountRepository.save(account);

        Optional<Account> result = accountRepository.findById(account.getId());

        assertTrue(result.isPresent());
        assertEquals(account.getId(), result.get().getId());
        assertEquals("Alice", result.get().getName());
        assertEquals("alice@mail.com", result.get().getEmail());
        assertEquals(BigDecimal.valueOf(100), result.get().getBalance());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenAccountNotFound() {
        var accountUserId = UUID.randomUUID();

        Optional<Account> result = accountRepository.findById(accountUserId);

        assertTrue(result.isEmpty());
    }
}
