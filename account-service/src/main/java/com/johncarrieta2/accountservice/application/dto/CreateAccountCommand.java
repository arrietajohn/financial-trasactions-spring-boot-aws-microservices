package com.cobre.accountservice.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class CreateAccountCommand {

    private final @NotBlank(message = "Name is required")
    String name;

    private final @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    String email;

    private final @NotNull(message = "Initial balance is required")
    @Positive(message = "Initial balance must be greater than zero")
    BigDecimal initialBalance;
}
