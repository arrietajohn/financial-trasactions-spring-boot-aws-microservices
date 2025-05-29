package com.cobre.paymentservice.application.usecases.payment;

import com.cobre.paymentservice.application.dto.ProcessPaymentCommand;
import com.cobre.paymentservice.application.dto.ProcessPaymentResponse;
import com.cobre.paymentservice.application.mapper.PaymentMapper;
import com.cobre.paymentservice.application.port.in.payment.IProcessPaymentUseCase;
import com.cobre.paymentservice.application.service.IPaymentPolicyService;
import com.cobre.paymentservice.domain.model.Payment;
import com.cobre.paymentservice.domain.model.PaymentStatus;
import com.cobre.paymentservice.domain.model.TransferErrorCode;
import com.cobre.paymentservice.domain.model.TransferResult;
import com.cobre.paymentservice.domain.port.out.INotifyPayment;
import com.cobre.paymentservice.domain.port.out.ISavePayment;
import com.cobre.paymentservice.domain.port.out.ITransferMoney;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;

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
        if (!isAmountValid(command.getAmount())) {
            var message = String.format(
                    "Amount %s is out of allowed range [%s, %s]",
                    command.getAmount(),
                    paymentPolicyService.getMinAmount(),
                    paymentPolicyService.getMaxAmount()
            );
            var paymentId = UUID.randomUUID();

            return new ProcessPaymentResponse(paymentId, PaymentStatus.FAILED, message);
        }

        Payment payment = buildInitialPayment(command);
        TransferResult transferResult = executeTransfer(payment);
        payment = updatePaymentWithResult(payment, transferResult);

        savePaymentPort.save(payment);
        notifyPaymentPort.notify(paymentMapper.toNotification(payment, transferResult.message()));

        return paymentMapper.toResponse(payment);
    }
    
    private boolean isAmountValid(BigDecimal amount) {
        return amount.compareTo(paymentPolicyService.getMinAmount()) >= 0 &&
                amount.compareTo(paymentPolicyService.getMaxAmount()) <= 0;
    }

    private Payment buildInitialPayment(ProcessPaymentCommand command) {
        BigDecimal tax = paymentPolicyService.calculateTax(command.getAmount());
        BigDecimal fee = paymentPolicyService.calculateFee(command.getAmount());
        return paymentMapper.toDomain(command, tax, fee);
    }

    private TransferResult executeTransfer(Payment payment) {
        try {
            return transferMoneyPort.transfer(
                    payment.getPayerId(),
                    payment.getRecipientId(),
                    payment.getTotalAmount(),
                    payment.getAmount()
            );
        } catch (Exception e) {
            return new TransferResult(
                    payment.getPayerId(),
                    TransferErrorCode.FAILED_INTERNAL_ERROR.getCode(),
                    e.getMessage() != null ? e.getMessage() : "Transfer failed",
                    null,// traceId
                    null,       // path
                    null,       // code
                    null,       // method
                    null,       // service
                    null,       // error
                    null,       // details
                    null,       // timestamp
                    500         // httpStatus
            );
        }
    }

    private Payment updatePaymentWithResult(Payment payment, TransferResult result) {
        return paymentMapper.withStatus(
                payment,
                result.isSuccess() ? PaymentStatus.SUCCESS : PaymentStatus.FAILED,
                result.status(),
                result.message()
        );
    }

}
