package com.cobre.accountservice.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdateAccountCommand {
    @NotNull(message = "Account ID is required")
    private UUID accountId;
    @PositiveOrZero(message = "Account balance must be greater than or equal to zero")
    private BigDecimal accountBalance;
    private String accountEmail;
}
