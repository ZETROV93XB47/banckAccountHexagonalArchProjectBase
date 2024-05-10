package com.example.bank.demo.domain.ports.mapper;

import com.example.bank.demo.domain.model.BankAccount;
import com.example.bank.demo.domain.dto.response.WithdrawalResponseDto;

public interface WithdrawalResponseDtoMapperPort {
    WithdrawalResponseDto mapToWithdrawalResponseDto(BankAccount account);
}
