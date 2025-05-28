package com.cobre.accountservice.infrastructure.adapter.controller;

import com.cobre.accountservice.application.dto.TransferMoneyCommand;
import com.cobre.accountservice.application.dto.TransferMoneyResponse;
import com.cobre.accountservice.application.port.in.transfer.ITransferMoneyUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final ITransferMoneyUseCase transferMoneyUseCase;

    @PostMapping
    public ResponseEntity<TransferMoneyResponse> transferMoney(
            @Valid @RequestBody TransferMoneyCommand command) {

        TransferMoneyResponse response = transferMoneyUseCase.transfer(command);
        return ResponseEntity.ok(response);
    }
}
