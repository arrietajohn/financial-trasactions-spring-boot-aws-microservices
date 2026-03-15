package com.cobre.accountservice.application.port.in.create;

import com.cobre.accountservice.application.dto.CreateAccountCommand;
import com.cobre.accountservice.domain.model.Account;

public interface ICreateAccountUseCase {

    Account handle(CreateAccountCommand command);
}
