package com.johncarrieta2.accountservice.infrastructure.persistence.entity;



import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountEntity {

    @Id
    private UUID id;

    private String name;

    private String email;

    @Column(nullable = false)
    private BigDecimal balance;
}
