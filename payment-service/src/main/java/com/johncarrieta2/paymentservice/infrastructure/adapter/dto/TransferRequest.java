package com.johncarrieta2.paymentservice.infrastructure.adapter.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(
        UUID senderAccountId,
        UUID recipientAccountId,
        BigDecimal amountToWithdraw,
        BigDecimal amountToTransfer
) {}
