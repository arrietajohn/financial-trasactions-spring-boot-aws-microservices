package com.cobre.accountservice.application.port.in.update;

import com.cobre.accountservice.application.dto.UpdateAccountCommand;
import com.cobre.accountservice.domain.model.Account;

public interface IUpdateAccountUseCase {
    Account handle(UpdateAccountCommand account);
}
