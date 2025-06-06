package com.cobre.accountservice.application.usecases.transfer;

import com.cobre.accountservice.application.dto.TransferMoneyCommand;
import com.cobre.accountservice.application.dto.TransferMoneyResponse;
import com.cobre.accountservice.application.port.in.transfer.ITransferMoneyUseCase;
import com.cobre.accountservice.domain.exceptions.AccountNotFoundException;
import com.cobre.accountservice.domain.model.Account;
import com.cobre.accountservice.domain.model.Transfer;
import com.cobre.accountservice.domain.model.TransactionTypeEnum;
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
    public TransferMoneyResponse  transfer(TransferMoneyCommand command) {
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

        return TransferMoneyResponse.builder()
                .senderAccountId(sender.getId())
                .status(TransactionTypeEnum.SUCCESS)
                .message("Transfer completed successfully")
                .build();
    }
}
