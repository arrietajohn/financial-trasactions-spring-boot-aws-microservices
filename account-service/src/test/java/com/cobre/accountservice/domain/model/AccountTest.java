package com.cobre.accountservice.domain.model;

import com.cobre.accountservice.domain.exceptions.InsufficientBalanceException;
import com.cobre.accountservice.domain.exceptions.InvalidAmountException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    @Test
    void credit_ShouldIncreaseBalance() {
        Account account = new Account("Alice", "alice@mail.com", BigDecimal.valueOf(100));
        account.credit(BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), account.getBalance());
    }

    @Test
    void debit_ShouldDecreaseBalance_WhenSufficientFunds() {
        Account account = new Account("Bob", "bob@mail.com", BigDecimal.valueOf(200));
        account.debit(BigDecimal.valueOf(75));

        assertEquals(BigDecimal.valueOf(125), account.getBalance());
    }

    @Test
    void debit_ShouldThrowException_WhenInsufficientFunds() {
        Account account = new Account("Charlie", "charlie@mail.com", BigDecimal.valueOf(50));

        assertThrows(InsufficientBalanceException.class, () -> {
            account.debit(BigDecimal.valueOf(100));
        });
    }

    @Test
    void debit_ShouldThrowException_WhenAmountIsInvalid() {
        Account account = new Account("David", "david@mail.com", BigDecimal.valueOf(50));

        assertAll(
                () -> assertThrows(InvalidAmountException.class, () -> account.debit(null)),
                () -> assertThrows(InvalidAmountException.class, () -> account.debit(BigDecimal.ZERO)),
                () -> assertThrows(InvalidAmountException.class, () -> account.debit(BigDecimal.valueOf(-10)))
        );
    }

    @Test
    void credit_ShouldThrowException_WhenAmountIsInvalid() {
        Account account = new Account("Eve", "eve@mail.com", BigDecimal.valueOf(50));

        assertAll(
                () -> assertThrows(InvalidAmountException.class, () -> account.credit(null)),
                () -> assertThrows(InvalidAmountException.class, () -> account.credit(BigDecimal.ZERO)),
                () -> assertThrows(InvalidAmountException.class, () -> account.credit(BigDecimal.valueOf(-5)))
        );
    }

    @Test
    void constructor_ShouldGenerateNonNullUUID() {
        Account account = new Account("Zoe", "zoe@mail.com", BigDecimal.valueOf(100));
        assertNotNull(account.getId());
        assertInstanceOf(UUID.class, account.getId());
    }
}
