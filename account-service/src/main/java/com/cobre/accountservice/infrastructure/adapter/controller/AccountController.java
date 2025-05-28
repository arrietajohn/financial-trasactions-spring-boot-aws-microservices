package com.cobre.accountservice.infrastructure.adapter.controller;

import com.cobre.accountservice.application.dto.CreateAccountCommand;
import com.cobre.accountservice.application.dto.CreateAccountResponse;
import com.cobre.accountservice.application.port.in.create.ICreateAccountUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final ICreateAccountUseCase accountService;

    @PostMapping
    public ResponseEntity<CreateAccountResponse> create(@Valid @RequestBody CreateAccountCommand request) {
        CreateAccountResponse response = accountService.handle(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
