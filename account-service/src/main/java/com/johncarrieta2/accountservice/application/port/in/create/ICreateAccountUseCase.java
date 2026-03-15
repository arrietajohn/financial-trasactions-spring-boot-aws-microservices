package com.johncarrieta2.accountservice.application.port.in.create;

import com.johncarrieta2.accountservice.application.dto.CreateAccountCommand;
import com.johncarrieta2.accountservice.domain.model.Account;

public interface ICreateAccountUseCase {

    Account handle(CreateAccountCommand command);
}
