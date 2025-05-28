package com.cobre.accountservice.application.usecase;

import com.cobre.accountservice.application.dto.TransferMoneyCommand;
import com.cobre.accountservice.application.dto.TransferMoneyResponse;
import com.cobre.accountservice.application.usecases.transfer.TransferMoneyHandler;
import com.cobre.accountservice.domain.exceptions.AccountNotFoundException;
import com.cobre.accountservice.domain.exceptions.InsufficientBalanceException;
import com.cobre.accountservice.domain.model.Account;
import com.cobre.accountservice.domain.model.TransferStatusEnum;
import com.cobre.accountservice.domain.port.out.IAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TransferMoneyHandlerTest {

    private IAccountRepository accountRepository;
    private TransferMoneyHandler handler;

    @BeforeEach
    void setUp() {
        accountRepository = Mockito.mock(IAccountRepository.class);
        handler = new TransferMoneyHandler(accountRepository);
    }

    @Test
    void shouldTransferSuccessfullyWhenAccountsAreValidAndBalanceIsSufficient() {
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();

        Account sender = new Account(senderId, "Sender", "sender@example.com", new BigDecimal("10000"));
        Account receiver = new Account(receiverId, "Receiver", "receiver@example.com", new BigDecimal("1000"));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        when(accountRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(accountRepository.findById(receiverId)).thenReturn(Optional.of(receiver));

        TransferMoneyCommand command = TransferMoneyCommand.builder()
                .senderAccountId(senderId)
                .recipientAccountId(receiverId)
                .amountToWithdraw(new BigDecimal("1000"))
                .amountToTransfer(new BigDecimal("900"))
                .build();

        TransferMoneyResponse response = handler.transfer(command);

        assertThat(response.getStatus()).isEqualTo(TransferStatusEnum.SUCCESS);
        assertThat(response.getSenderAccountId()).isEqualTo(senderId);

        verify(accountRepository).save(sender);
        verify(accountRepository).save(receiver);
    }

    @Test
    void shouldReturnErrorWhenSenderAccountNotFound() {
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();

        when(accountRepository.findById(senderId)).thenReturn(Optional.empty());

        TransferMoneyCommand command = TransferMoneyCommand.builder()
                .senderAccountId(senderId)
                .recipientAccountId(receiverId)
                .amountToWithdraw(new BigDecimal("1000"))
                .amountToTransfer(new BigDecimal("900"))
                .build();

        assertThrows(AccountNotFoundException.class, () -> handler.transfer(command));
    }

    @Test
    void shouldReturnErrorWhenInsufficientBalance() {
        UUID senderId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();

        Account sender = new Account(senderId, "Sender", "sender@example.com", new BigDecimal("500"));
        Account receiver = new Account(receiverId, "Receiver", "receiver@example.com", new BigDecimal("1000"));

        when(accountRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(accountRepository.findById(receiverId)).thenReturn(Optional.of(receiver));

        TransferMoneyCommand command = TransferMoneyCommand.builder()
                .senderAccountId(senderId)
                .recipientAccountId(receiverId)
                .amountToWithdraw(new BigDecimal("1000"))
                .amountToTransfer(new BigDecimal("900"))
                .build();

        assertThrows(InsufficientBalanceException.class, () -> handler.transfer(command));

    }
}
