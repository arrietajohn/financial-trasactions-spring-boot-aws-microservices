package com.cobre.accountservice.application.usecases.get;


import com.cobre.accountservice.application.dto.FindAccountQuery;
import com.cobre.accountservice.application.port.in.get.IGetAccountUseCase;
import com.cobre.accountservice.domain.exceptions.AccountNotFoundException;
import com.cobre.accountservice.domain.model.Account;
import com.cobre.accountservice.domain.port.out.IAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAccounHandler implements IGetAccountUseCase {
    private final IAccountRepository accountRepository;

    @Override
    public Account handle(FindAccountQuery query) {
        return accountRepository.findById(query.getAccountId()).orElseThrow(()
                -> new AccountNotFoundException("Account not found with ID: " + query.getAccountId()));

    }
}
