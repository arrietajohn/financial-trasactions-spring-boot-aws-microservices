package com.johncarrieta2.paymentservice.application.service;

import java.math.BigDecimal;

public interface IPaymentPolicyService {
    BigDecimal getMinAmount();
    BigDecimal getMaxAmount();
    BigDecimal calculateTax(BigDecimal amount);
    BigDecimal calculateFee(BigDecimal amount);
}
