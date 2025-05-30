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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
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
        log.info("Received ProcessPaymentCommand: {}", command);

        if (!isAmountValid(command.getAmount())) {
            var message = String.format(
                    "Amount %s is out of allowed range [%s, %s]",
                    command.getAmount(),
                    paymentPolicyService.getMinAmount(),
                    paymentPolicyService.getMaxAmount()
            );
            var paymentId = UUID.randomUUID();

            log.warn("Payment failed due to invalid amount: {}", message);

            return new ProcessPaymentResponse(paymentId, PaymentStatus.FAILED, message);
        }

        Payment payment = buildInitialPayment(command);
        log.debug("Initial payment built: {}", payment);

        TransferResult transferResult = executeTransfer(payment);
        log.info("TransferResult received: {}", transferResult);

        payment = updatePaymentWithResult(payment, transferResult);
        log.debug("Payment updated with transfer result: {}", payment);

        log.info("Saving payment with ID: {}", payment.getPaymentId());
        savePaymentPort.save(payment);

        log.info("Notifying about payment with ID: {}", payment.getPaymentId());
        notifyPaymentPort.notify(paymentMapper.toNotification(payment, transferResult.message()));

        ProcessPaymentResponse response = paymentMapper.toResponse(payment);
        log.info("Returning ProcessPaymentResponse: {}", response);

        return response;
    }

    private boolean isAmountValid(BigDecimal amount) {
        boolean valid = amount.compareTo(paymentPolicyService.getMinAmount()) >= 0 &&
                amount.compareTo(paymentPolicyService.getMaxAmount()) <= 0;
        log.debug("Checking if amount {} is valid (min: {}, max: {}): {}",
                amount, paymentPolicyService.getMinAmount(), paymentPolicyService.getMaxAmount(), valid);
        return valid;
    }

    private Payment buildInitialPayment(ProcessPaymentCommand command) {
        log.debug("Building initial payment for command: {}", command);

        BigDecimal tax = paymentPolicyService.calculateTax(command.getAmount());
        log.debug("Calculated tax for amount {}: {}", command.getAmount(), tax);

        BigDecimal fee = paymentPolicyService.calculateFee(command.getAmount());
        log.debug("Calculated fee for amount {}: {}", command.getAmount(), fee);

        Payment payment = paymentMapper.toDomain(command, tax, fee);
        log.debug("Mapped payment domain object: {}", payment);

        return payment;
    }

    private TransferResult executeTransfer(Payment payment) {
        log.info("Attempting to transfer money for payment ID: {}", payment.getPaymentId());

        try {
            TransferResult result = transferMoneyPort.transfer(
                    payment.getPayerId(),
                    payment.getRecipientId(),
                    payment.getTotalAmount(),
                    payment.getAmount()
            );
            log.info("Money transfer successful for payment ID: {}, result: {}", payment.getPaymentId(), result);
            return result;
        } catch (Exception e) {
            log.error("Money transfer failed for payment ID: {}: {}", payment.getPaymentId(), e.getMessage(), e);
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
        log.debug("Updating payment status for payment ID: {}. TransferResult: {}", payment.getPaymentId(), result);
        Payment updatedPayment = paymentMapper.withStatus(
                payment,
                result.isSuccess() ? PaymentStatus.SUCCESS : PaymentStatus.FAILED,
                result.status(),
                result.message()
        );
        log.debug("Updated payment: {}", updatedPayment);
        return updatedPayment;
    }

}
