package com.cobre.accountservice.domain.model;

import com.cobre.accountservice.domain.exceptions.InsufficientBalanceException;
import com.cobre.accountservice.domain.exceptions.InvalidAmountException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void credit_ShouldIncreaseBalance() {
        Account account = new Account("1", "Alice", "alice@mail.com", BigDecimal.valueOf(100));
        account.credit(BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), account.getBalance());
    }

    @Test
    void debit_ShouldDecreaseBalance_WhenSufficientFunds() {
        Account account = new Account("1", "Alice", "alice@mail.com", BigDecimal.valueOf(200));
        account.debit(BigDecimal.valueOf(75));

        assertEquals(BigDecimal.valueOf(125), account.getBalance());
    }

    @Test
    void debit_ShouldThrowException_WhenInsufficientFunds() {
        Account account = new Account("1", "Alice", "alice@mail.com", BigDecimal.valueOf(50));

        InsufficientBalanceException ex = assertThrows(InsufficientBalanceException.class, () -> {
            account.debit(BigDecimal.valueOf(100));
        });

        assertEquals("Insufficient balance.", ex.getMessage());
    }

    @Test
    void debit_ShouldThrowException_WhenAmountIsInvalid() {
        Account account = new Account("1", "Alice", "alice@mail.com", BigDecimal.valueOf(50));

        assertAll(
                () -> assertThrows(InvalidAmountException.class, () -> account.debit(null)),
                () -> assertThrows(InvalidAmountException.class, () -> account.debit(BigDecimal.ZERO)),
                () -> assertThrows(InvalidAmountException.class, () -> account.debit(BigDecimal.valueOf(-10)))
        );
    }

    @Test
    void credit_ShouldThrowException_WhenAmountIsInvalid() {
        Account account = new Account("1", "Alice", "alice@mail.com", BigDecimal.valueOf(50));

        assertAll(
                () -> assertThrows(InvalidAmountException.class, () -> account.credit(null)),
                () -> assertThrows(InvalidAmountException.class, () -> account.credit(BigDecimal.ZERO)),
                () -> assertThrows(InvalidAmountException.class, () -> account.credit(BigDecimal.valueOf(-5)))
        );
    }
}