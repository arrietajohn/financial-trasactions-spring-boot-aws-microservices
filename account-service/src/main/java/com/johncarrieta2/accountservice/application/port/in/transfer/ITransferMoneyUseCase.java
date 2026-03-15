package com.cobre.accountservice.application.port.in.transfer;

import com.cobre.accountservice.application.dto.TransferMoneyCommand;
import com.cobre.accountservice.application.dto.TransferMoneyResponse;

public interface ITransferMoneyUseCase {
    TransferMoneyResponse transfer(TransferMoneyCommand command);
}
