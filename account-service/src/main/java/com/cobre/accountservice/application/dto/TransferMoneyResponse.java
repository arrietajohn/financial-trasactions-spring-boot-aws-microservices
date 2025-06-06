package com.cobre.accountservice.application.dto;

import com.cobre.accountservice.domain.model.TransactionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferMoneyResponse {
    private UUID senderAccountId;
    private TransactionTypeEnum status;
    private String message;
}
