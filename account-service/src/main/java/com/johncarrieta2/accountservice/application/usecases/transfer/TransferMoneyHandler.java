package com.johncarrieta2.accountservice.application.usecases.transfer;

import com.johncarrieta2.accountservice.application.dto.TransferMoneyCommand;
import com.johncarrieta2.accountservice.application.dto.TransferMoneyResponse;
import com.johncarrieta2.accountservice.application.port.in.transfer.ITransferMoneyUseCase;
import com.johncarrieta2.accountservice.domain.exceptions.AccountNotFoundException;
import com.johncarrieta2.accountservice.domain.model.Account;
import com.johncarrieta2.accountservice.domain.model.Transfer;
import com.johncarrieta2.accountservice.domain.model.TransferStatusEnum;
import com.johncarrieta2.accountservice.domain.port.out.IAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferMoneyHandler implements ITransferMoneyUseCase {

    private final IAccountRepository accountRepository;

    @Override
    @Transactional
    public TransferMoneyResponse transfer(final TransferMoneyCommand command) {
        final Transfer transfer = new Transfer(
                command.getSenderAccountId(),
                command.getRecipientAccountId(),
                command.getAmountToWithdraw(),
                command.getAmountToTransfer()
        );

        final Account sender = accountRepository.findById(transfer.getSenderAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Sender account not found"));

        final Account receiver = accountRepository.findById(transfer.getReceiverAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Receiver account not found"));

        sender.debit(transfer.getAmountToWithdraw());
        receiver.credit(transfer.getAmountToTransfer());

        accountRepository.update(sender);
        accountRepository.update(receiver);

        return TransferMoneyResponse.builder()
                .senderAccountId(sender.getId())
                .status(TransferStatusEnum.SUCCESS)
                .message("Transfer completed successfully")
                .build();
    }
}
