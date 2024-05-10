package com.example.bank.demo.application.controller.api;

import com.example.bank.demo.domain.service.BankOperationsUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "WithdrawalController")
public class WithdrawalController {

    private final BankOperationsUseCase bankOperationsUseCase;

    /*
    @PostMapping(value = "/bank/services/withdrawal", produces = APPLICATION_JSON_VALUE)
    public WithdrawalResponseDto makeWithdrawal(@Valid @RequestBody WithdrawalRequestDto requestDto) {
        return bankOperationsUseCase.makeWithdrawal(requestDto.getWithdrawalAmount(), requestDto.getAccountId());
    }

     */
}
