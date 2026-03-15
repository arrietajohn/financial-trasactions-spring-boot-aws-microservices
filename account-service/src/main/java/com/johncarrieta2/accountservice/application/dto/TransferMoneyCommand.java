package com.cobre.accountservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@Builder
public class TransferMoneyCommand {

    private final @NotNull(message = "Sender account ID is required")
    UUID senderAccountId;

    private final @NotNull(message = "Recipient account ID is required")
    UUID recipientAccountId;

    private final @NotNull(message = "Amount to withdraw is required")
    @Positive(message = "Amount to withdraw must be greater than zero")
    BigDecimal amountToWithdraw;

    private final @NotNull(message = "Amount to transfer is required")
    @Positive(message = "Amount to transfer must be greater than zero")
    BigDecimal amountToTransfer;
}
