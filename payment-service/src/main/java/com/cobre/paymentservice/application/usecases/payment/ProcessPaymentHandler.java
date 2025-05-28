package com.cobre.paymentservice.application.usecases.payment;

import com.cobre.paymentservice.application.dto.ProcessPaymentCommand;
import com.cobre.paymentservice.application.dto.ProcessPaymentResponse;
import com.cobre.paymentservice.application.port.in.payment.IProcessPaymentUseCase;
import com.cobre.paymentservice.application.service.IPaymentPolicyService;
import com.cobre.paymentservice.domain.model.Payment;
import com.cobre.paymentservice.domain.model.PaymentStatus;
import com.cobre.paymentservice.domain.port.out.INotifyPayment;
import com.cobre.paymentservice.domain.port.out.ISavePayment;
import com.cobre.paymentservice.domain.port.out.ITransferMoney;
import org.springframework.stereotype.Service;

@Service
public class ProcessPaymentHandler implements IProcessPaymentUseCase {

    private final IPaymentPolicyService paymentPolicyService;
    private final ISavePayment savePaymentPort;
    private final ITransferMoney transferMoneyPort;
    private final INotifyPayment notifyPaymentPort;

    public ProcessPaymentHandler(IPaymentPolicyService paymentPolicyService,
                                 ISavePayment savePaymentPort,
                                 ITransferMoney transferMoneyPort,
                                 INotifyPayment notifyPaymentPort) {
        this.paymentPolicyService = paymentPolicyService;
        this.savePaymentPort = savePaymentPort;
        this.transferMoneyPort = transferMoneyPort;
        this.notifyPaymentPort = notifyPaymentPort;
    }

    @Override
    public ProcessPaymentResponse handle(ProcessPaymentCommand command) {
        var amount = command.getAmount();

        if (amount.compareTo(paymentPolicyService.getMinAmount()) < 0 ||
                amount.compareTo(paymentPolicyService.getMaxAmount()) > 0) {
            return new ProcessPaymentResponse(null, PaymentStatus.FAILED, "Amount out of allowed range");
        }

        var tax = paymentPolicyService.calculateTax(amount);
        var fee = paymentPolicyService.calculateFee(amount);

        var payment = new Payment(
                null,
                command.getPayerId(),
                command.getRecipientId(),
                amount,
                tax,
                fee,
                command.getReason(),
                PaymentStatus.PENDING,
                null
        );

        try {
            transferMoneyPort.transfer(
                    payment.getPayerId(),
                    payment.getRecipientId(),
                    payment.getTotalAmount(),
                    payment.getAmount()
            );
            payment = new Payment(
                    payment.getPaymentId(),
                    payment.getPayerId(),
                    payment.getRecipientId(),
                    payment.getAmount(),
                    payment.getTax(),
                    payment.getFee(),
                    payment.getReason(),
                    PaymentStatus.SUCCESS,
                    payment.getTimestamp()
            );
        } catch (Exception e) {
            payment = new Payment(
                    payment.getPaymentId(),
                    payment.getPayerId(),
                    payment.getRecipientId(),
                    payment.getAmount(),
                    payment.getTax(),
                    payment.getFee(),
                    payment.getReason(),
                    PaymentStatus.FAILED,
                    payment.getTimestamp()
            );
        }

        savePaymentPort.save(payment);
        notifyPaymentPort.notify(payment.getPaymentId());

        return new ProcessPaymentResponse(
                payment.getPaymentId(),
                payment.getStatus(),
                payment.getStatus() == PaymentStatus.SUCCESS ? "Payment completed" : "Payment failed"
        );
    }
}
