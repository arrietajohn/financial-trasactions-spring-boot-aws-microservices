package com.johncarrieta2.accountservice.application.usecases.create;

import com.johncarrieta2.accountservice.application.dto.CreateAccountCommand;
import com.johncarrieta2.accountservice.application.mapper.AccountMapper;
import com.johncarrieta2.accountservice.application.port.in.create.ICreateAccountUseCase;
import com.johncarrieta2.accountservice.domain.port.out.IAccountRepository;
import com.johncarrieta2.accountservice.domain.model.Account;
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
