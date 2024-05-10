package com.example.bank.demo.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Entity
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Table(name = "bank_account")
@NoArgsConstructor
public class BankAccount {
    @Id
    @Column(name = "accountid",  nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(name = "accountnumber",  columnDefinition = "BINARY(16) NOT NULL UNIQUE", nullable = false)
    private UUID accountNumber;

    @Column(name = "balance",  nullable = false)
    private BigDecimal balance;

    @Column(name = "overdraft",  nullable = false)
    private BigDecimal montantAutorisationDeDecouvert;

//    @Column(name = "depositLimit",  nullable = false)
//    private final BigDecimal depositLimit;
}
