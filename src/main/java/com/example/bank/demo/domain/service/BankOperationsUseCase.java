package com.example.bank.demo.domain.service;

import com.example.bank.demo.domain.ports.useCase.MakeDepositUseCase;
import com.example.bank.demo.domain.ports.useCase.MakeWithDrawalUseCase;
import com.example.bank.demo.domain.dto.response.DepositResponseDto;
import com.example.bank.demo.domain.dto.response.WithdrawalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BankOperationsUseCase {

    private final MakeDepositUseCase makeDepositUseCase;
    private final MakeWithDrawalUseCase makeWithDrawalUseCase;

    public DepositResponseDto makeDeposit(BigDecimal depositValue, Long accountId) {
        return makeDepositUseCase.makeDeposit(depositValue, accountId);
    }

    public WithdrawalResponseDto makeWithdrawal(BigDecimal withdrawalAmount, Long accountId) {
        return makeWithDrawalUseCase.makeWithdrawal(withdrawalAmount, accountId);
    }
}
