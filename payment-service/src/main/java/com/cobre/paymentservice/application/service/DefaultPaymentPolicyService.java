package com.johncarrieta2.paymentservice.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class DefaultPaymentPolicyService implements IPaymentPolicyService {

    @Value("${payment.rules.minAmount}")
    private BigDecimal minAmount;

    @Value("${payment.rules.maxAmount}")
    private BigDecimal maxAmount;

    @Value("${payment.rules.taxRate}")
    private BigDecimal taxRate;

    @Value("${payment.rules.feeRate}")
    private BigDecimal feeRate;

    @Override
    public BigDecimal getMinAmount() {
        return minAmount;
    }

    @Override
    public BigDecimal getMaxAmount() {
        return maxAmount;
    }

    @Override
    public BigDecimal calculateTax(BigDecimal amount) {
        return amount.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        return amount.multiply(feeRate).setScale(2, RoundingMode.HALF_UP);
    }
}
