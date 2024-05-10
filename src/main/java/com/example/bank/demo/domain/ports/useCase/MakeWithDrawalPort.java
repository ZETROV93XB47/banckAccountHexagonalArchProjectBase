package com.example.bank.demo.domain.ports.useCase;

import com.example.bank.demo.domain.dto.response.WithdrawalResponseDto;

import java.math.BigDecimal;

public interface MakeWithDrawalPort {
    WithdrawalResponseDto makeWithdrawal(BigDecimal withdrawalValue, Long accountId);
}
