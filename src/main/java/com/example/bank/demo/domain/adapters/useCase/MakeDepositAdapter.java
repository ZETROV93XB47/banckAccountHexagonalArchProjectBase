package com.example.bank.demo.domain.adapters.useCase;

import com.example.bank.demo.domain.exceptions.AccountNotFoundException;
import com.example.bank.demo.domain.model.BankAccount;
import com.example.bank.demo.domain.model.Operation;
import com.example.bank.demo.domain.ports.mapper.DepositResponseDtoMapperPort;
import com.example.bank.demo.domain.ports.repository.BankAccountRepositoryPort;
import com.example.bank.demo.domain.ports.repository.OperationRepositoryPort;
import com.example.bank.demo.domain.ports.useCase.MakeDepositPort;
import com.example.bank.demo.domain.dto.response.DepositResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static com.example.bank.demo.domain.model.enumpackage.TypeOperation.DEPOT;

@Slf4j
@Component
@RequiredArgsConstructor
public class MakeDepositAdapter implements MakeDepositPort {

    private final OperationRepositoryPort operationRepositoryPort;
    private final BankAccountRepositoryPort bankAccountRepositoryPort;
    private final DepositResponseDtoMapperPort depositResponseDtoMapperPort;
    private static final Logger LOGGER = LoggerFactory.getLogger(MakeDepositAdapter.class);

    @Override
    @Transactional
    public DepositResponseDto makeDeposit(final BigDecimal depositValue, final Long accountId) {
        BankAccount accountForDeposit;
        Optional<BankAccount> account = bankAccountRepositoryPort.findById(accountId);

        if (account.isPresent()) {
            accountForDeposit = account.get();
        } else {
            throw new AccountNotFoundException("Account not found fro id : " + accountId);
        }

        BigDecimal previousBalance = accountForDeposit.getBalance();
        accountForDeposit.setBalance(previousBalance.add(depositValue));
        bankAccountRepositoryPort.save(accountForDeposit);

        //TODO: prendre en compte le livret A, ils ont un plafond de dépot
        saveAssociatedOperation(depositValue, accountForDeposit);

        return depositResponseDtoMapperPort.mapToDepositResponseDto(accountForDeposit);
    }

    private void saveAssociatedOperation(BigDecimal depositValue, BankAccount accountForDeposit) {
        Operation operation = new Operation(null, accountForDeposit, DEPOT, depositValue, new Date());
        operationRepositoryPort.save(operation);
    }
}
