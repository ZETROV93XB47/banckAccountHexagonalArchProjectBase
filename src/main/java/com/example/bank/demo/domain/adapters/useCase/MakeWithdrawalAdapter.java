package com.example.bank.demo.domain.adapters.useCase;

import com.example.bank.demo.domain.exceptions.AccountNotFoundException;
import com.example.bank.demo.domain.exceptions.WithdrawalAmountBiggerThanBalanceException;
import com.example.bank.demo.domain.model.BankAccount;
import com.example.bank.demo.domain.model.Operation;
import com.example.bank.demo.domain.ports.mapper.WithdrawalResponseDtoMapperPort;
import com.example.bank.demo.domain.ports.repository.BankAccountRepositoryPort;
import com.example.bank.demo.domain.ports.repository.OperationRepositoryPort;
import com.example.bank.demo.domain.ports.useCase.MakeWithDrawalPort;
import com.example.bank.demo.domain.dto.response.WithdrawalResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static com.example.bank.demo.domain.model.enumpackage.TypeOperation.RETRAIT;

@Slf4j
@Component
@RequiredArgsConstructor
public class MakeWithdrawalAdapter implements MakeWithDrawalPort {

    private final OperationRepositoryPort operationRepositoryPort;
    private final BankAccountRepositoryPort bankAccountRepositoryPort;
    private final WithdrawalResponseDtoMapperPort withdrawalResponseDtoMapperPort;
    private static final Logger LOGGER = LoggerFactory.getLogger(MakeWithdrawalAdapter.class);

    @Override
    public WithdrawalResponseDto makeWithdrawal(BigDecimal withdrawalValue, Long accountId) {

        BankAccount accountForWithdrawal;
        Optional<BankAccount> account = bankAccountRepositoryPort.findById(accountId);

        if (account.isPresent()) {
            accountForWithdrawal = account.get();
        } else {
            throw new AccountNotFoundException("Account not found for id : " + accountId);
        }

        BigDecimal currentBalance = accountForWithdrawal.getBalance();

        if (withdrawalValue.compareTo(currentBalance.add(accountForWithdrawal.getMontantAutorisationDeDecouvert())) == 1) {
            throw new WithdrawalAmountBiggerThanBalanceException("Le montant que vous tentez de retirer est supérieur au montant autorisé pour votre compte");
        } else {
            accountForWithdrawal.setBalance(currentBalance.subtract(withdrawalValue));
            bankAccountRepositoryPort.save(accountForWithdrawal);

            //TODO: prendre en compte le livret A, ils ont un plafond de dépot
            saveAssociatedOperation(withdrawalValue, accountForWithdrawal);
            return withdrawalResponseDtoMapperPort.mapToWithdrawalResponseDto(accountForWithdrawal);
        }
    }

    private void saveAssociatedOperation(BigDecimal withdrawalValue, BankAccount accountForWithdrawal) {
        LOGGER.info("Saving withdrawal Start");

        Operation operation = new Operation(null, accountForWithdrawal, RETRAIT, withdrawalValue, new Date());
        operationRepositoryPort.save(operation);

        LOGGER.info("Saving withdrawal Done");
        LOGGER.info("Withdrawal : {}", accountForWithdrawal);
    }
}

