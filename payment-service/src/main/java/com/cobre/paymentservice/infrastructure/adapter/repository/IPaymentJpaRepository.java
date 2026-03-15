package com.johncarrieta2.paymentservice.infrastructure.adapter.repository;

import com.johncarrieta2.paymentservice.infrastructure.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IPaymentJpaRepository extends JpaRepository<PaymentEntity, UUID> {

}