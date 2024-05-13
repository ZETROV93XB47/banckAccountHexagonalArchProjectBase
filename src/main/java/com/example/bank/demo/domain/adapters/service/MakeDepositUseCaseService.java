package com.example.bank.demo.domain.adapters.service;

import com.example.bank.demo.domain.dto.response.DepositResponseDto;
import com.example.bank.demo.domain.exceptions.AccountNotFoundException;
import com.example.bank.demo.domain.exceptions.DepositLimitExceededException;
import com.example.bank.demo.domain.model.Bank;
import com.example.bank.demo.domain.model.BankAccount;
import com.example.bank.demo.domain.model.Operation;
import com.example.bank.demo.domain.model.SavingAccount;
import com.example.bank.demo.domain.ports.mapper.DepositResponseDtoMapperPort;
import com.example.bank.demo.infrastructure.repository.BankAccountRepository;
import com.example.bank.demo.infrastructure.repository.BankRepository;
import com.example.bank.demo.infrastructure.repository.OperationRepository;
import com.example.bank.demo.infrastructure.repository.SavingAccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.bank.demo.domain.model.enumpackage.TypeOperation.DEPOT;

@Slf4j
@Component
@RequiredArgsConstructor
public class MakeDepositUseCaseService implements com.example.bank.demo.domain.ports.useCase.MakeDepositUseCase {

    private final BankRepository bankRepository;
    private final OperationRepository operationRepository;
    private final BankAccountRepository bankAccountRepository;
    private final SavingAccountRepository savingAccountRepository;
    private final DepositResponseDtoMapperPort depositResponseDtoMapperPort;
    private static final Logger LOGGER = LoggerFactory.getLogger(MakeDepositUseCaseService.class);

    @Override
    @Transactional
    public DepositResponseDto makeDeposit(final BigDecimal depositValue, final Long accountId) {
        Bank accountForDeposit;

        Optional<Bank> account = bankRepository.findById(accountId);

        if (account.isPresent()) {
            accountForDeposit = account.get();
        } else {
            throw new AccountNotFoundException("Account not found for id : " + accountId);
        }

        return switch (accountForDeposit.getAccountType()) {
            case SAVING_ACCOUNT -> makeDepositOnSavingAccount((SavingAccount) accountForDeposit, depositValue);
            case CLASSIC_ACCOUNT -> makeDepositOnNormalAccount((BankAccount)accountForDeposit, depositValue);
        };
    }

    private DepositResponseDto makeDepositOnNormalAccount(final BankAccount bankAccount, final BigDecimal depositValue) {
        BigDecimal currentBalance = bankAccount.getBalance();
        bankAccount.setBalance(currentBalance.add(depositValue));
        bankAccountRepository.save(bankAccount);
        saveAssociatedOperation(depositValue, bankAccount);

        return depositResponseDtoMapperPort.mapToDepositResponseDto(bankAccount);
    }

    private DepositResponseDto makeDepositOnSavingAccount(final SavingAccount savingAccount, final BigDecimal depositValue) {
        BigDecimal currentBalance = savingAccount.getBalance();

        if(currentBalance.add(depositValue).compareTo(savingAccount.getDepositLimit()) > 0) {
            throw new DepositLimitExceededException("Your Deposit value exceed the deposit limit on your account ");
        }

        savingAccount.setBalance(currentBalance.add(depositValue));
        savingAccountRepository.save(savingAccount);
        saveAssociatedOperation(depositValue, savingAccount);

        return depositResponseDtoMapperPort.mapToDepositResponseDto(savingAccount);
    }

    private void saveAssociatedOperation(BigDecimal depositValue, Bank accountForDeposit) {
        Operation operation = new Operation(null, accountForDeposit, DEPOT, depositValue, accountForDeposit.getAccountType(), LocalDateTime.now());
        operationRepository.save(operation);
    }
}
