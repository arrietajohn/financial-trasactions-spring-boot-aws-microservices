package com.cobre.accountservice.application.usecases.transfer;

import com.cobre.accountservice.application.dto.TransferMoneyCommand;
import com.cobre.accountservice.application.dto.TransferMoneyResponse;
import com.cobre.accountservice.application.port.in.transfer.ITransferMoneyUseCase;
import com.cobre.accountservice.domain.exceptions.AccountNotFoundException;
import com.cobre.accountservice.domain.exceptions.InsufficientBalanceException;
import com.cobre.accountservice.domain.exceptions.InvalidAmountException;
import com.cobre.accountservice.domain.exceptions.InvalidTransferAmountException;
import com.cobre.accountservice.domain.model.Account;
import com.cobre.accountservice.domain.model.Transfer;
import com.cobre.accountservice.domain.model.TransferStatusEnum;
import com.cobre.accountservice.domain.port.out.IAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferMoneyHandler implements ITransferMoneyUseCase {


    private final IAccountRepository accountRepository;

    @Override
    @Transactional
    public TransferMoneyResponse transfer(TransferMoneyCommand command) {
        try {
            Transfer transfer = new Transfer(
                    command.getSenderAccountId(),
                    command.getRecipientAccountId(),
                    command.getAmountToWithdraw(),
                    command.getAmountToTransfer()
            );

            Account sender = accountRepository.findById(transfer.getSenderAccountId())
                    .orElseThrow(() -> new AccountNotFoundException("Sender account not found"));

            Account receiver = accountRepository.findById(transfer.getReceiverAccountId())
                    .orElseThrow(() -> new AccountNotFoundException("Receiver account not found"));

            sender.debit(transfer.getAmountToWithdraw());

            receiver.credit(transfer.getAmountToTransfer());

            accountRepository.update(sender);
            accountRepository.update(receiver);

            return createTransferResponse(command, TransferStatusEnum.SUCCESS, "Transfer completed successfully");

        } catch (AccountNotFoundException ex) {
            return createTransferResponse(command, TransferStatusEnum.FAILED_ACCOUNT_NOT_FOUND, ex.getMessage());
        } catch (InsufficientBalanceException ex) {
            return createTransferResponse(command, TransferStatusEnum.FAILED_INSUFFICIENT_BALANCE, ex.getMessage());
        } catch (InvalidAmountException | InvalidTransferAmountException ex) {
            return createTransferResponse(command, TransferStatusEnum.FAILED_INVALID_AMOUNT, ex.getMessage());
        } catch (Exception ex) {
            return createTransferResponse(command, TransferStatusEnum.FAILED_INTERNAL_ERROR, ex.getMessage());
        }
    }

    private TransferMoneyResponse createTransferResponse(TransferMoneyCommand command, TransferStatusEnum status, String message) {
        return TransferMoneyResponse.builder()
                .senderAccountId(command.getSenderAccountId())
                .status(status)
                .message(message)
                .build();
    }
}
