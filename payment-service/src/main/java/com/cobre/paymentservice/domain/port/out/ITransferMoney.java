package com.cobre.paymentservice.domain.port.out;

import com.cobre.paymentservice.domain.model.TransferResult;

import java.math.BigDecimal;
import java.util.UUID;

public interface ITransferMoney {
    TransferResult transfer(UUID payerId, UUID recipientId, BigDecimal amountToWithdraw, BigDecimal amountToTransfer);
}