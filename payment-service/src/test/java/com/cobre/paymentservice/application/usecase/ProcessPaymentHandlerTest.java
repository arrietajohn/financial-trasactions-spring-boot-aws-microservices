package com.cobre.paymentservice.application.usecase;

import com.cobre.paymentservice.application.dto.ProcessPaymentCommand;
import com.cobre.paymentservice.application.dto.ProcessPaymentResponse;
import com.cobre.paymentservice.application.mapper.PaymentMapper;
import com.cobre.paymentservice.application.usecases.payment.ProcessPaymentHandler;
import com.cobre.paymentservice.application.service.IPaymentPolicyService;
import com.cobre.paymentservice.domain.model.PaymentStatus;
import com.cobre.paymentservice.domain.model.TransferResult;
import com.cobre.paymentservice.domain.port.out.INotifyPayment;
import com.cobre.paymentservice.domain.port.out.ISavePayment;
import com.cobre.paymentservice.domain.port.out.ITransferMoney;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessPaymentHandlerTest {

    @Mock IPaymentPolicyService policyService;
    @Mock ISavePayment savePayment;
    @Mock ITransferMoney transferMoney;
    @Mock INotifyPayment notifyPayment;
    @Spy  PaymentMapper mapper = new PaymentMapper();
    @InjectMocks ProcessPaymentHandler handler;

    @BeforeEach
    void setUp() {

    }

    @Test
    void shouldProcessValidPaymentSuccessfully() {
        UUID payerId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("10000.00");

        TransferResult successfulTransferResult = new TransferResult(
                payerId, "SUCCESS", "Payment completed", // Changed message here
                UUID.randomUUID().toString(), "/path", "SUCCESS", "POST",
                "account-service", "NONE", null, "2023-01-01T00:00:00Z", 200
        );

        when(policyService.getMinAmount()).thenReturn(new BigDecimal("5000.00"));
        when(policyService.getMaxAmount()).thenReturn(new BigDecimal("100000.00"));
        when(policyService.calculateTax(amount)).thenReturn(new BigDecimal("300.00"));
        when(policyService.calculateFee(amount)).thenReturn(new BigDecimal("500.00"));
        when(transferMoney.transfer(any(UUID.class), any(UUID.class), any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(successfulTransferResult);

        ProcessPaymentCommand command = new ProcessPaymentCommand(payerId, recipientId, amount, "Prueba exitosa");
        ProcessPaymentResponse response = handler.handle(command);

        assertEquals(PaymentStatus.SUCCESS, response.getStatus());
        assertEquals("Payment completed", response.getMessage()); // This assertion should now pass
        verify(transferMoney, times(1)).transfer(any(), any(), any(), any());
        verify(savePayment, times(1)).save(any());
        verify(notifyPayment, times(1)).notify(any());
    }

    @Test
    void shouldFailPaymentIfAmountOutOfRange() {
        UUID payerId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("2000.00");

        when(policyService.getMinAmount()).thenReturn(new BigDecimal("5000.00"));
        when(policyService.getMaxAmount()).thenReturn(new BigDecimal("100000.00")); // Or a higher value if needed

        ProcessPaymentCommand command = new ProcessPaymentCommand(payerId, recipientId, amount, "Monto insuficiente");
        ProcessPaymentResponse response = handler.handle(command);

        assertEquals(PaymentStatus.FAILED, response.getStatus());
        assertEquals("Amount 2000.00 is out of allowed range [5000.00, 100000.00]", response.getMessage());
        verifyNoInteractions(transferMoney);
        verifyNoInteractions(savePayment);
        verifyNoInteractions(notifyPayment);
    }
}
