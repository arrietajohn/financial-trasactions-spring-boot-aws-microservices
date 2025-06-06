package com.cobre.accountservice.infrastructure.adapter.dto;

import com.cobre.accountservice.domain.model.AccountStatusEnum;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountResponse {
    private UUID accountId;
    private AccountStatusEnum status;
    private String message;

}
