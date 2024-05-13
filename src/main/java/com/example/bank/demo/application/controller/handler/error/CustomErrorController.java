package com.example.bank.demo.application.controller.handler.error;


import com.example.bank.demo.application.controller.handler.error.model.ErrorResponseDto;
import com.example.bank.demo.domain.exceptions.AccountNotFoundException;
import com.example.bank.demo.domain.exceptions.DepositLimitExceededException;
import com.example.bank.demo.domain.exceptions.WithdrawalAmountBiggerThanBalanceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.example.bank.demo.application.controller.handler.error.enumpackage.ErrorCode.*;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(WithdrawalAmountBiggerThanBalanceException.class)
    public ResponseEntity<ErrorResponseDto> handleWithdrawalAmountBiggerThanBalanceException(WithdrawalAmountBiggerThanBalanceException exception) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(exception.getMessage(), WITHDRAWAL_EXCEEDING_OVERDRAFT.getResponseStatus().value(), WITHDRAWAL_EXCEEDING_OVERDRAFT.getErrorCodeType());
        return new ResponseEntity<>(errorResponseDto, WITHDRAWAL_EXCEEDING_OVERDRAFT.getResponseStatus());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleAccountNotFoundException(AccountNotFoundException exception) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(exception.getMessage(), ACCOUNT_NOT_FOUND.getResponseStatus().value(), ACCOUNT_NOT_FOUND.getErrorCodeType());
        return new ResponseEntity<>(errorResponseDto, ACCOUNT_NOT_FOUND.getResponseStatus());
    }

    @ExceptionHandler(DepositLimitExceededException.class)
    public ResponseEntity<ErrorResponseDto> handleDepositLimitExceededException(DepositLimitExceededException exception) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(exception.getMessage(), DEPOSIT_LIMIT_REACHED.getResponseStatus().value(), DEPOSIT_LIMIT_REACHED.getErrorCodeType());
        return new ResponseEntity<>(errorResponseDto, DEPOSIT_LIMIT_REACHED.getResponseStatus());
    }

}