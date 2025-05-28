package com.cobre.paymentservice.application.usecase;

import com.cobre.paymentservice.application.dto.ProcessPaymentCommand;
import com.cobre.paymentservice.application.dto.ProcessPaymentResponse;
import com.cobre.paymentservice.application.mapper.PaymentMapper;
import com.cobre.paymentservice.application.port.in.payment.IProcessPaymentUseCase;
import com.cobre.paymentservice.application.service.IPaymentPolicyService;
import com.cobre.paymentservice.application.usecases.payment.ProcessPaymentHandler;
import com.cobre.paymentservice.domain.model.PaymentStatus;
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
        policyService = mock(IPaymentPolicyService.class);
        savePayment = mock(ISavePayment.class);
        transferMoney = mock(ITransferMoney.class);
        notifyPayment = mock(INotifyPayment.class);
        handler = new ProcessPaymentHandler(policyService, savePayment, transferMoney, notifyPayment, mapper);
    }

    @Test
    void shouldProcessValidPaymentSuccessfully() {
        UUID payerId = UUID.randomUUID();
        UUID recipientId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("10000.00");

        when(policyService.getMinAmount()).thenReturn(new BigDecimal("5000.00"));
        when(policyService.getMaxAmount()).thenReturn(new BigDecimal("100000.00"));
        when(policyService.calculateTax(amount)).thenReturn(new BigDecimal("300.00"));
        when(policyService.calculateFee(amount)).thenReturn(new BigDecimal("500.00"));

        ProcessPaymentCommand command = new ProcessPaymentCommand(payerId, recipientId, amount, "Prueba exitosa");
        ProcessPaymentResponse response = handler.handle(command);

        assertEquals(PaymentStatus.SUCCESS, response.getStatus());
        assertEquals("Payment completed", response.getMessage());
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
        ProcessPaymentCommand command = new ProcessPaymentCommand(payerId, recipientId, amount, "Monto insuficiente");
        ProcessPaymentResponse response = handler.handle(command);

        assertEquals(PaymentStatus.FAILED, response.getStatus());
        assertEquals("Amount out of allowed range", response.getMessage());
        verifyNoInteractions(transferMoney);
        verifyNoInteractions(savePayment);
        verifyNoInteractions(notifyPayment);
    }

}
