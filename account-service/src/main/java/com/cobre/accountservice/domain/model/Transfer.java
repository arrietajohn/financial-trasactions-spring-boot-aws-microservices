package com.cobre.accountservice.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Transfer {

    private final UUID senderAccountId;
    private final UUID receiverAccountId;
    private final BigDecimal transferAmount;

    public Transfer(UUID senderAccountId, UUID receiverAccountId, BigDecimal transferAmount) {
        if (senderAccountId == null || receiverAccountId == null) {
            throw new IllegalArgumentException("Account IDs cannot be null");
        }
        if (transferAmount == null || transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.transferAmount = transferAmount;
    }

    public UUID getSenderAccountId() {
        return senderAccountId;
    }

    public UUID getReceiverAccountId() {
        return receiverAccountId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }
}