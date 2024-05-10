package com.example.bank.demo.domain.service;

import com.example.bank.demo.domain.ports.useCase.MakeDepositPort;
import com.example.bank.demo.domain.ports.useCase.MakeWithDrawalPort;
import com.example.bank.demo.domain.dto.response.DepositResponseDto;
import com.example.bank.demo.domain.dto.response.WithdrawalResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BankOperationsUseCase {

    private final MakeDepositPort makeDepositPort;
    private final MakeWithDrawalPort makeWithDrawalPort;

    public DepositResponseDto makeDeposit(BigDecimal depositValue, Long accountId) {
        return makeDepositPort.makeDeposit(depositValue, accountId);
    }

    public WithdrawalResponseDto makeWithdrawal(BigDecimal withdrawalAmount, Long accountId) {
        return makeWithDrawalPort.makeWithdrawal(withdrawalAmount, accountId);
    }
}
