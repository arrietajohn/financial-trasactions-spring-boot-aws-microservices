package com.cobre.accountservice.domain.model;

import com.cobre.accountservice.domain.exceptions.InsufficientBalanceException;
import com.cobre.accountservice.domain.exceptions.InvalidAmountException;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {

    private final String id;
    private String name;
    private String email;
    private BigDecimal balance;

    public Account(String id, String name, String email, BigDecimal balance) {
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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
