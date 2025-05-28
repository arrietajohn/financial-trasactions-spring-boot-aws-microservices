package com.cobre.accountservice.infrastructure.adapter.controller;

import com.cobre.accountservice.application.dto.CreateAccountCommand;
import com.cobre.accountservice.application.dto.CreateAccountResponse;
import com.cobre.accountservice.application.port.in.create.ICreateAccountUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "Transfer API", description = "Money transfer operations between accounts")
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final ICreateAccountUseCase accountService;

    @Operation(summary = "Create a new account")
    @ApiResponse(responseCode = "201", description = "Account created")
    @PostMapping
    public ResponseEntity<CreateAccountResponse> create(@Valid @RequestBody CreateAccountCommand request) {
        CreateAccountResponse response = accountService.handle(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
