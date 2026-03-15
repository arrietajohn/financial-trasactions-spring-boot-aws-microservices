package com.johncarrieta2.accountservice.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class FindAccountQuery {
    @NotNull(message = "Account ID is required")
    @NotEmpty(message = "Account ID is required")
    private final UUID accountId;
}
