package com.johncarrieta2.accountservice.infrastructure.adapter.controller;

import com.johncarrieta2.accountservice.application.dto.TransferMoneyCommand;
import com.johncarrieta2.accountservice.application.dto.TransferMoneyResponse;
import com.johncarrieta2.accountservice.application.port.in.transfer.ITransferMoneyUseCase;
import com.johncarrieta2.accountservice.infrastructure.adapter.dto.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Transfer API", description = "Money transfer operations between accounts")
@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final ITransferMoneyUseCase transferMoneyUseCase;

    @Operation(
            summary = "Transfer money between accounts",
            description = "Transfers a specified amount from one account to another",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Transfer successful",
                            content = @Content(schema = @Schema(implementation = TransferMoneyResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Business rule or validation error",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Unexpected internal error",
                            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<TransferMoneyResponse> transferMoney(
            @Valid @RequestBody final TransferMoneyCommand command) {

        final TransferMoneyResponse response = transferMoneyUseCase.transfer(command);
        return ResponseEntity.ok(response);
    }
}
