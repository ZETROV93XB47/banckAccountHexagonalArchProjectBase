package com.example.bank.demo.domain.adapters.service;

import com.example.bank.demo.domain.dto.response.WithdrawalResponseDto;
import com.example.bank.demo.domain.exceptions.AccountNotFoundException;
import com.example.bank.demo.domain.exceptions.WithdrawalAmountBiggerThanBalanceException;
import com.example.bank.demo.domain.model.Bank;
import com.example.bank.demo.domain.model.BankAccount;
import com.example.bank.demo.domain.model.Operation;
import com.example.bank.demo.domain.model.SavingAccount;
import com.example.bank.demo.domain.ports.mapper.WithdrawalResponseDtoMapperPort;
import com.example.bank.demo.domain.ports.useCase.MakeWithDrawalUseCase;
import com.example.bank.demo.domain.utils.DateProvider;
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
import java.util.Optional;

import static com.example.bank.demo.domain.model.enumpackage.TypeOperation.RETRAIT;

@Slf4j
@Component
@RequiredArgsConstructor
public class MakeWithdrawalUseCaseService implements MakeWithDrawalUseCase {

    private final DateProvider dateProvider;
    private final BankRepository bankRepository;
    private final OperationRepository operationRepository;
    private final BankAccountRepository bankAccountRepository;
    private final SavingAccountRepository savingAccountRepository;
    private final WithdrawalResponseDtoMapperPort withdrawalResponseDtoMapperPort;
    private static final Logger LOGGER = LoggerFactory.getLogger(MakeWithdrawalUseCaseService.class);

    @Override
    @Transactional
    public WithdrawalResponseDto makeWithdrawal(final BigDecimal withdrawalValue, final Long accountId) {
        Bank accountForWithdrawal;

        Optional<? extends Bank> account = bankRepository.findById(accountId);

        if (account.isPresent()) {
            accountForWithdrawal = account.get();
        } else {
            throw new AccountNotFoundException("Account not found for id : " + accountId);
        }

        return switch (accountForWithdrawal.getAccountType()) {
            case SAVING_ACCOUNT -> makeWithdrawalOnSavingAccount((SavingAccount) accountForWithdrawal, withdrawalValue);
            case CLASSIC_ACCOUNT -> makeWithdrawaltOnNormalAccount((BankAccount)accountForWithdrawal, withdrawalValue);
        };
    }

    private WithdrawalResponseDto makeWithdrawaltOnNormalAccount(final BankAccount bankAccount, final BigDecimal withdrawalValue) {
        BigDecimal currentBalance = bankAccount.getBalance();

        if(currentBalance.subtract(withdrawalValue).compareTo(bankAccount.getOverdraftLimit()) < 0) {
            throw new WithdrawalAmountBiggerThanBalanceException("Le montant que vous tentez de retirer est supérieur au montant autorisé pour votre compte");
        }

        bankAccount.setBalance(currentBalance.subtract(withdrawalValue));
        bankAccountRepository.save(bankAccount);
        saveAssociatedOperation(withdrawalValue, bankAccount);

        return withdrawalResponseDtoMapperPort.mapToWithdrawalResponseDto(bankAccount);
    }

    private WithdrawalResponseDto makeWithdrawalOnSavingAccount(final SavingAccount savingAccount, final BigDecimal withdrawalValue) {
        BigDecimal currentBalance = savingAccount.getBalance();

        if(currentBalance.subtract(withdrawalValue).compareTo(new BigDecimal(0)) < 0) {
            throw new WithdrawalAmountBiggerThanBalanceException("Le montant que vous tentez de retirer est supérieur au montant autorisé pour votre compte");
        }

        savingAccount.setBalance(currentBalance.subtract(withdrawalValue));
        savingAccountRepository.save(savingAccount);
        saveAssociatedOperation(withdrawalValue, savingAccount);

        return withdrawalResponseDtoMapperPort.mapToWithdrawalResponseDto(savingAccount);
    }

    private void saveAssociatedOperation(BigDecimal withdrawalValue, Bank accountForwithdrawal) {
        Operation operation = new Operation(null, accountForwithdrawal, RETRAIT, withdrawalValue, accountForwithdrawal.getAccountType(), dateProvider.getCurrentDate());
        operationRepository.save(operation);
    }

}

