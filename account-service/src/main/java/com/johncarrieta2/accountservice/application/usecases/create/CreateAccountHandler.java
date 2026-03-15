package com.cobre.accountservice.application.usecases.create;

import com.cobre.accountservice.application.dto.CreateAccountCommand;
import com.cobre.accountservice.application.mapper.AccountMapper;
import com.cobre.accountservice.application.port.in.create.ICreateAccountUseCase;
import com.cobre.accountservice.domain.port.out.IAccountRepository;
import com.cobre.accountservice.domain.model.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateAccountHandler implements ICreateAccountUseCase {

    private final IAccountRepository accountRepository;

    @Override
    public Account handle(final CreateAccountCommand command) {
        if (Objects.isNull(command.getInitialBalance()) || command.getInitialBalance().signum() < 0) {
            throw new IllegalArgumentException("Initial balance must be non-negative");
        }

        final Account newAccount = AccountMapper.toDomain(command);
        accountRepository.save(newAccount);
        return newAccount;
    }
}
