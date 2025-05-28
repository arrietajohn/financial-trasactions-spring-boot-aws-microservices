package com.cobre.paymentservice.domain.port.out;

import java.math.BigDecimal;
import java.util.UUID;

public interface TransferMoneyPort {
    void transfer(UUID payerId, UUID recipientId, BigDecimal amountToWithdraw, BigDecimal amountToTransfer);
}