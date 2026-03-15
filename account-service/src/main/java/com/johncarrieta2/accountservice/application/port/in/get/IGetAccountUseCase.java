package com.johncarrieta2.accountservice.application.port.in.get;

import com.johncarrieta2.accountservice.application.dto.FindAccountQuery;
import com.johncarrieta2.accountservice.domain.model.Account;

public interface IGetAccountUseCase {
     Account handle(FindAccountQuery query) ;
}
