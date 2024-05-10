package com.example.bank.demo.domain.ports.repository;

import com.example.bank.demo.domain.model.Operation;

public interface OperationRepositoryPort {
    void save(Operation account);
}
