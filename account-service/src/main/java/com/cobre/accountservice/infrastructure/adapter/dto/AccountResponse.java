package com.cobre.accountservice.infrastructure.adapter.dto;

import com.cobre.accountservice.domain.model.AccountStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;


public record AccountResponse(UUID id, String name, String email, BigDecimal balance, AccountStatusEnum status) {

}
