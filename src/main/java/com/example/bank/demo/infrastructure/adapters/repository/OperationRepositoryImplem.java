package com.example.bank.demo.infrastructure.adapters.repository;

import com.example.bank.demo.domain.model.Operation;
import com.example.bank.demo.domain.ports.repository.OperationRepositoryPort;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OperationRepositoryImplem implements OperationRepositoryPort {

    private final EntityManager entityManager;

    @Override
    @Transactional
    public void save(Operation operation) {
        entityManager.persist(operation);
    }
}
