package com.example.bank.demo.domain.ports.repository;

import com.example.bank.demo.domain.model.BankAccount;

import java.util.Optional;

public interface BankAccountRepositoryPort {
    Optional<BankAccount> findById(Long id);
    void save(BankAccount bankAccount);
}
