package com.example.bank.demo.infrastructure.adapters.repository;

import com.example.bank.demo.domain.model.BankAccount;
import com.example.bank.demo.domain.ports.repository.BankAccountRepositoryPort;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BankAccountRepositoryImplem implements BankAccountRepositoryPort {

    private final EntityManager entityManager;

    @Override
    @Transactional
    public Optional<BankAccount> findById(Long id) {
        return Optional.of(entityManager.find(BankAccount.class, id));
    }

    @Override
    @Transactional
    public void save(BankAccount bankAccount) {
        entityManager.persist(bankAccount);
    }
}
