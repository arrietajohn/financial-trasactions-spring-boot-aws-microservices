package com.cobre.accountservice.application.dto;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateAccountResponse {
    private final UUID accountId;
}
