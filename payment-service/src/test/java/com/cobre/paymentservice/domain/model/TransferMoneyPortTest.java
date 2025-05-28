package com.cobre.paymentservice.domain.model;

import com.cobre.paymentservice.domain.port.out.ITransferMoney;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TransferMoneyPortTest {

    private ITransferMoney transferMoneyPort;

    @BeforeEach
    void setUp() {
        transferMoneyPort = mock(ITransferMoney.class);
    }

    @Test
    void shouldCallTransferOnceWithValidParameters() {
        UUID payerId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        BigDecimal amountToWithdraw = new BigDecimal("10800.00");
        BigDecimal amountToTransfer = new BigDecimal("10000.00");

        transferMoneyPort.transfer(payerId, recipientId, amountToWithdraw, amountToTransfer);

        verify(transferMoneyPort, times(1)).transfer(payerId, recipientId, amountToWithdraw, amountToTransfer);
    }

    @Test
    void shouldThrowExceptionWhenTransferFails() {
        UUID payerId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        BigDecimal amountToWithdraw = new BigDecimal("10800.00");
        BigDecimal amountToTransfer = new BigDecimal("10000.00");

        doThrow(new RuntimeException("Transfer failed")).when(transferMoneyPort)
                .transfer(payerId, recipientId, amountToWithdraw, amountToTransfer);

        assertThrows(RuntimeException.class, () ->
                transferMoneyPort.transfer(payerId, recipientId, amountToWithdraw, amountToTransfer));

        verify(transferMoneyPort, times(1)).transfer(payerId, recipientId, amountToWithdraw, amountToTransfer);
    }
}
