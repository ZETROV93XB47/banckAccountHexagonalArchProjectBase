package com.example.bank.demo.domain.ports.useCase;

import com.example.bank.demo.domain.dto.response.DepositResponseDto;

import java.math.BigDecimal;

public interface MakeDepositPort {
    DepositResponseDto makeDeposit(BigDecimal depositValue, Long accountId);
}
