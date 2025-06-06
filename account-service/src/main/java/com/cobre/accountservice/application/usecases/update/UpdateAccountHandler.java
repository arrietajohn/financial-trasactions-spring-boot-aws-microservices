package com.cobre.accountservice.application.usecases.update;

import com.cobre.accountservice.application.dto.UpdateAccountCommand;
import com.cobre.accountservice.application.port.in.update.IUpdateAccountUseCase;
import com.cobre.accountservice.domain.exceptions.AccountNotFoundException;
import com.cobre.accountservice.domain.model.Account;
import com.cobre.accountservice.domain.model.AccountStatusEnum;
import com.cobre.accountservice.domain.port.out.IAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateAccountHandler implements IUpdateAccountUseCase {

    private final IAccountRepository accountRepository;
    @Override
    public Account handle(UpdateAccountCommand command) {
        if (command == null || command.getAccountId() == null) {
            throw new IllegalArgumentException("Account and Account ID must not be null");
        }
        var currentAccount = accountRepository.findById(command.getAccountId()).orElseThrow(() ->
            new AccountNotFoundException("Account not found with ID: " + command.getAccountId()));

        var  accountToUpdate =  new Account(
                currentAccount.getId(),
                currentAccount.getName(),
                (command.getAccountEmail() !=  null) ? command.getAccountEmail() : currentAccount.getEmail(),
                currentAccount.getBalance().add(command.getAccountBalance()),
                AccountStatusEnum.UPDATED);
        accountRepository.update(accountToUpdate);
        return accountToUpdate;
    }
}
