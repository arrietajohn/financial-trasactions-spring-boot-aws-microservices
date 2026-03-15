package com.cobre.accountservice.application.port.in.get;

import com.cobre.accountservice.application.dto.FindAccountQuery;
import com.cobre.accountservice.domain.model.Account;

public interface IGetAccountUseCase {
     Account handle(FindAccountQuery query) ;
}
