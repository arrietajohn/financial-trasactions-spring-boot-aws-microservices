package com.cobre.accountservice.domain.model;

import com.cobre.accountservice.domain.exceptions.InvalidTransferAmountException;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class Transfer {

    private final UUID senderAccountId;
    private final UUID receiverAccountId;
    private final BigDecimal amountToWithdraw;
    private final BigDecimal amountToTransfer;

    public Transfer(UUID senderAccountId, UUID receiverAccountId,
                    BigDecimal amountToWithdraw, BigDecimal amountToTransfer) {

        if (senderAccountId == null || receiverAccountId == null) {
            throw new InvalidTransferAmountException("Sender and receiver account IDs cannot be null");
        }

        if (amountToWithdraw == null || amountToWithdraw.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferAmountException("Amount to withdraw must be greater than zero");
        }

        if (amountToTransfer == null || amountToTransfer.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferAmountException("Amount to transfer must be greater than zero");
        }

        if (amountToTransfer.compareTo(amountToWithdraw) > 0) {
            throw new InvalidTransferAmountException("Amount to transfer cannot be greater than amount to withdraw");
        }


        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.amountToWithdraw = amountToWithdraw;
        this.amountToTransfer = amountToTransfer;
    }
}