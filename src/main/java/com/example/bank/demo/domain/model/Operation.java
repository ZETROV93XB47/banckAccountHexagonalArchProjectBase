package com.example.bank.demo.domain.model;

import com.example.bank.demo.domain.model.enumpackage.TypeOperation;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Table(name = "operation")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ManyToOne
    @JoinColumn(name = "accountid", nullable = false)
    private final BankAccount compteBancaire;

    @Enumerated(EnumType.STRING)
    private final TypeOperation typeOperation;

    @Column(nullable = false)
    private final BigDecimal montant;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private final Date dateOperation;
}