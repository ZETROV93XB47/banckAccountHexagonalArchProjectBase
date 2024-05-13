package com.example.bank.demo.domain.adapters.service;

import com.example.bank.demo.domain.dto.response.OperationReportResponseDto;
import com.example.bank.demo.domain.exceptions.AccountNotFoundException;
import com.example.bank.demo.domain.exceptions.OperationNotFoundException;
import com.example.bank.demo.domain.model.Bank;
import com.example.bank.demo.domain.model.Operation;
import com.example.bank.demo.domain.ports.mapper.OperationReportResponseDtoMapperPort;
import com.example.bank.demo.infrastructure.repository.BankRepository;
import com.example.bank.demo.infrastructure.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OperationsReportUseCaseService implements com.example.bank.demo.domain.ports.useCase.OperationsReportUseCase {

    private final BankRepository bankRepository;
    private final OperationRepository operationRepository;
    private final OperationReportResponseDtoMapperPort operationReportResponseDtoMapperPort;

    @Override
    public OperationReportResponseDto getOperationsReport(Long accountId) {
        Bank accountForOperations;

        Optional<Bank> account = bankRepository.findById(accountId);

        if (account.isPresent()) {
            accountForOperations = account.get();
        } else {
            throw new AccountNotFoundException("Account not found for id : " + accountId);
        }

        List<Operation> operations = operationRepository.findByAccountIdOrderByDateOperationDesc(accountForOperations)
                .orElseThrow(() -> new OperationNotFoundException("No Operation found for account Id : " + accountId));

        return operationReportResponseDtoMapperPort.mapToOperationResponseDto(operations, accountForOperations.getAccountNumber());
    }
}
