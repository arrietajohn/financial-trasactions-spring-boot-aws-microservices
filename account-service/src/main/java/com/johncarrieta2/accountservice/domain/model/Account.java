package com.johncarrieta2.accountservice.domain.model;

import com.johncarrieta2.accountservice.domain.exceptions.InsufficientBalanceException;
import com.johncarrieta2.accountservice.domain.exceptions.InvalidAmountException;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Builder(toBuilder = true)
@Getter
public class Account {

    private final UUID id;
    private final String name;
    private final String email;

    @Getter
    private BigDecimal balance;

    public Account(final String name, final String email, final BigDecimal balance) {
        this(UUID.randomUUID(), name, email, balance);
    }

    public Account(final UUID id, final String name, final String email, final BigDecimal balance) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.email = Objects.requireNonNull(email);
        this.balance = Objects.requireNonNull(balance);
    }

    public void debit(final BigDecimal amount) {
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance.");
        }
        this.balance = this.balance.subtract(amount);
    }

    public void credit(final BigDecimal amount) {
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }
        this.balance = this.balance.add(amount);
    }

    private void ejemplo() {
        final Account account = Account.builder()
                .name("John Arrieta")
                .email("dadsads")
                .build();
        final var account2 = account.toBuilder().balance(BigDecimal.TEN).build();
    }
}
