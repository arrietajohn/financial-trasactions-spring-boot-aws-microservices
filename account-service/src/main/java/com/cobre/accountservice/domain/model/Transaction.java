package com.cobre.accountservice.domain.model;

import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Value
public class Transaction {
    UUID id;
    Instant timestamp;
    Account account;
    BigDecimal amount;
    TransactionStatus status;
    String description;
}
