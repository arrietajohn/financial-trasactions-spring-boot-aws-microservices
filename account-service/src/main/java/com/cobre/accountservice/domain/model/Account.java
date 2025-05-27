package com.cobre.accountservice.domain.model;

import com.cobre.accountservice.domain.exceptions.InsufficientBalanceException;
import com.cobre.accountservice.domain.exceptions.InvalidAmountException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Account {

    private final UUID id;
    private final String name;
    private final String email;

    @Getter
    private BigDecimal balance;

    public Account(String name, String email, BigDecimal balance) {
        this(UUID.randomUUID(), name, email, balance);
    }

    public Account(UUID id, String name, String email, BigDecimal balance) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.email = Objects.requireNonNull(email);
        this.balance = Objects.requireNonNull(balance);
    }

    public void debit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance.");
        }
        this.balance = this.balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }
        this.balance = this.balance.add(amount);
    }
}
