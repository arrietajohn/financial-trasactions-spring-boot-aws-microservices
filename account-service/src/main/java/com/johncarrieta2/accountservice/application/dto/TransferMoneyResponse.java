package com.cobre.accountservice.application.dto;

import com.cobre.accountservice.domain.model.TransferStatusEnum;
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
    private TransferStatusEnum status;
    private String message;
}
