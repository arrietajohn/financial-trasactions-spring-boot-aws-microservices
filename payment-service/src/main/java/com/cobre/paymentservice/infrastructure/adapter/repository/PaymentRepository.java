package com.johncarrieta2.paymentservice.infrastructure.adapter.repository;

import com.johncarrieta2.paymentservice.domain.model.Payment;
import com.johncarrieta2.paymentservice.domain.port.out.ISavePayment;
import com.johncarrieta2.paymentservice.infrastructure.persistence.entity.PaymentEntity;
import com.johncarrieta2.paymentservice.infrastructure.persistence.mapper.PaymentEntityMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepository implements ISavePayment {

    private final IPaymentJpaRepository jpaRepository;
    private final PaymentEntityMapper mapper;

    public PaymentRepository(IPaymentJpaRepository jpaRepository, PaymentEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(Payment payment) {
        PaymentEntity entity = mapper.toEntity(payment);
        jpaRepository.save(entity);
    }
}
