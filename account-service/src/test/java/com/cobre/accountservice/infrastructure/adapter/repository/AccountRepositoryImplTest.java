package com.cobre.accountservice.infrastructure.adapter.repository;



import com.cobre.accountservice.infrastructure.persistence.entity.AccountEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AccountRepositoryImplTest {

    @Autowired
    private IAccountJpaRepository jpaRepository;

    @Test
    void shouldSaveAccountEntity() {
        AccountEntity entity = new AccountEntity();
        entity.setId(UUID.fromString(UUID.randomUUID().toString()));
        entity.setName("Test User");
        entity.setEmail("test@example.com");
        entity.setBalance(BigDecimal.valueOf(150));

        AccountEntity saved = jpaRepository.save(entity);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Test User");
        assertThat(saved.getEmail()).isEqualTo("test@example.com");
        assertThat(saved.getBalance()).isEqualTo(BigDecimal.valueOf(150));
    }
}
