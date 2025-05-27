package com.cobre.accountservice.application.port.in.create;

import com.cobre.accountservice.application.dto.CreateAccountCommand;
import com.cobre.accountservice.application.dto.CreateAccountResponse;

public interface ICreateAccountUseCase {

    CreateAccountResponse handle(CreateAccountCommand command);
}
