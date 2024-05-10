package com.example.bank.demo.application.controller.api;

import com.example.bank.demo.domain.ports.useCase.MakeDepositPort;
import com.example.bank.demo.domain.ports.useCase.MakeWithDrawalPort;
import com.example.bank.demo.domain.dto.request.DepositRequestDto;
import com.example.bank.demo.domain.dto.request.WithdrawalRequestDto;
import com.example.bank.demo.domain.dto.response.DepositResponseDto;
import com.example.bank.demo.domain.dto.response.WithdrawalResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Tag(name = "BankAccountController")
public class BankAccountController {

    private final MakeDepositPort makeDepositPort;
    private final MakeWithDrawalPort makeWithDrawalPort;
    //private final BankOperationsUseCase bankOperationsUseCase;

    @PostMapping(value = "/bank/services/deposit", produces = APPLICATION_JSON_VALUE)
    public DepositResponseDto makeDeposit(@Valid @RequestBody DepositRequestDto requestDto) {
        return makeDepositPort.makeDeposit(requestDto.getDepositAmount(), requestDto.getAccountId());
    }

    @PostMapping(value = "/bank/services/withdrawal", produces = APPLICATION_JSON_VALUE)
    public WithdrawalResponseDto makeWithdrawal(@Valid @RequestBody WithdrawalRequestDto requestDto) {
        return makeWithDrawalPort.makeWithdrawal(requestDto.getWithdrawalAmount(), requestDto.getAccountId());
    }

    @GetMapping(value = "/bank/services/healthCheck", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getReceiptsByIdInOrder() {
        return ok("Hello Guys !!! 😁🔥🔥🔥");
    }

}
