package com.cobre.accountservice.infrastructure.adapter.controller;

import com.cobre.accountservice.application.dto.CreateAccountCommand;
import com.cobre.accountservice.application.dto.FindAccountQuery;
import com.cobre.accountservice.application.dto.UpdateAccountCommand;
import com.cobre.accountservice.application.mapper.AccountMapper;
import com.cobre.accountservice.application.port.in.get.IGetAccountUseCase;
import com.cobre.accountservice.application.port.in.update.IUpdateAccountUseCase;
import com.cobre.accountservice.infrastructure.adapter.dto.AccountResponse;
import com.cobre.accountservice.infrastructure.adapter.dto.CreateAccountResponse;
import com.cobre.accountservice.application.port.in.create.ICreateAccountUseCase;
import com.cobre.accountservice.infrastructure.adapter.dto.UpdateAccountResponse;
import com.cobre.accountservice.infrastructure.adapter.mapper.AccountResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.UUID;


@Tag(name = "Account API", description = "Account manager use cases")
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final ICreateAccountUseCase accountService;
    private final IGetAccountUseCase getAccountUsecase;
    private final IUpdateAccountUseCase updateAccountUsecase;

    @Operation(summary = "Create a new account")
    @ApiResponse(responseCode = "201", description = "Account created")
    @PostMapping
    public ResponseEntity<CreateAccountResponse> create(@Valid @RequestBody CreateAccountCommand request) {
        var accountedCreated = accountService.handle(request);
        var response = AccountMapper.toResponse(accountedCreated);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get account by ID")
    @ApiResponse(responseCode = "200", description = "Account found")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> get(@PathVariable UUID accountId) {
        return ResponseEntity.ok(AccountMapper.toGetResponse(getAccountUsecase.handle(new FindAccountQuery(accountId))));
    }

    @Operation(summary = "Update an existing account")
    @ApiResponse(responseCode = "200", description = "Account updated")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PutMapping
    public ResponseEntity<UpdateAccountResponse> update(@Valid @RequestBody UpdateAccountCommand command) {
        var accountUpdated = updateAccountUsecase.handle(command);
        return ResponseEntity.ok(AccountResponseMapper.toUpdateResponse(accountUpdated, "Account updated successfully"));
    }
}
