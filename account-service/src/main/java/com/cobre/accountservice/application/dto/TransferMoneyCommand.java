package com.cobre.accountservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferMoneyCommand {

    @NotNull(message = "Sender account ID is required")
    private UUID senderAccountId;

    @NotNull(message = "Recipient account ID is required")
    private UUID recipientAccountId;

    @NotNull(message = "Amount to withdraw is required")
    @Positive(message = "Amount to withdraw must be greater than zero")
    private BigDecimal amountToWithdraw;

    @NotNull(message = "Amount to transfer is required")
    @Positive(message = "Amount to transfer must be greater than zero")
    private BigDecimal amountToTransfer;
}

