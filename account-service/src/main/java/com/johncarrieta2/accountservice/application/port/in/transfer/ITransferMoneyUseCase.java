package com.johncarrieta2.accountservice.application.port.in.transfer;

import com.johncarrieta2.accountservice.application.dto.TransferMoneyCommand;
import com.johncarrieta2.accountservice.application.dto.TransferMoneyResponse;

public interface ITransferMoneyUseCase {
    TransferMoneyResponse transfer(TransferMoneyCommand command);
}
