package com.cobre.paymentservice.application.usecases.payment;

import com.cobre.paymentservice.application.dto.ProcessPaymentCommand;
import com.cobre.paymentservice.application.dto.ProcessPaymentResponse;
import com.cobre.paymentservice.application.mapper.PaymentMapper;
import com.cobre.paymentservice.application.port.in.payment.IProcessPaymentUseCase;
import com.cobre.paymentservice.application.service.IPaymentPolicyService;
import com.cobre.paymentservice.domain.model.Payment;
import com.cobre.paymentservice.domain.model.PaymentStatus;
import com.cobre.paymentservice.domain.port.out.INotifyPayment;
import com.cobre.paymentservice.domain.port.out.ISavePayment;
import com.cobre.paymentservice.domain.port.out.ITransferMoney;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProcessPaymentHandler implements IProcessPaymentUseCase {

    private final IPaymentPolicyService paymentPolicyService;
    private final ISavePayment savePaymentPort;
    private final ITransferMoney transferMoneyPort;
    private final INotifyPayment notifyPaymentPort;
    private final PaymentMapper paymentMapper;

    public ProcessPaymentHandler(IPaymentPolicyService paymentPolicyService,
                                 ISavePayment savePaymentPort,
                                 ITransferMoney transferMoneyPort,
                                 INotifyPayment notifyPaymentPort,
                                 PaymentMapper paymentMapper) {
        this.paymentPolicyService = paymentPolicyService;
        this.savePaymentPort = savePaymentPort;
        this.transferMoneyPort = transferMoneyPort;
        this.notifyPaymentPort = notifyPaymentPort;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public ProcessPaymentResponse handle(ProcessPaymentCommand command) {
        BigDecimal amount = command.getAmount();

        if (amount.compareTo(paymentPolicyService.getMinAmount()) < 0 ||
                amount.compareTo(paymentPolicyService.getMaxAmount()) > 0) {
            return new ProcessPaymentResponse(null, PaymentStatus.FAILED, "Amount out of allowed range");
        }

        BigDecimal tax = paymentPolicyService.calculateTax(amount);
        BigDecimal fee = paymentPolicyService.calculateFee(amount);

        Payment payment = paymentMapper.toDomain(command, tax, fee);

        try {
            transferMoneyPort.transfer(
                    payment.getPayerId(),
                    payment.getRecipientId(),
                    payment.getTotalAmount(),
                    payment.getAmount()
            );
            payment = paymentMapper.withStatus(payment, PaymentStatus.SUCCESS);
        } catch (Exception e) {
            payment = paymentMapper.withStatus(payment, PaymentStatus.FAILED);
        }

        savePaymentPort.save(payment);
        notifyPaymentPort.notify(payment.getPaymentId());

        return paymentMapper.toResponse(payment);
    }
}
