package com.johncarrieta2.accountservice.application.usecases.get;


import com.johncarrieta2.accountservice.application.dto.FindAccountQuery;
import com.johncarrieta2.accountservice.application.port.in.get.IGetAccountUseCase;
import com.johncarrieta2.accountservice.domain.exceptions.AccountNotFoundException;
import com.johncarrieta2.accountservice.domain.model.Account;
import com.johncarrieta2.accountservice.domain.port.out.IAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAccounHandler implements IGetAccountUseCase {
    private final IAccountRepository accountRepository;

    @Override
    public Account handle(final FindAccountQuery query) {
        return accountRepository.findById(query.getAccountId()).orElseThrow(()
                -> new AccountNotFoundException("Account not found with ID: " + query.getAccountId()));

    }
}
