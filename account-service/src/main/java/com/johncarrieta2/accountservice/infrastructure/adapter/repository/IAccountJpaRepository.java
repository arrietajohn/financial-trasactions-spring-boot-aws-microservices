package com.johncarrieta2.accountservice.infrastructure.adapter.repository;

import com.johncarrieta2.accountservice.infrastructure.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IAccountJpaRepository extends JpaRepository<AccountEntity, UUID> {

}
