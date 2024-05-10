package com.example.bank.demo.application.controller.handler.error;


import com.example.bank.demo.application.controller.handler.error.model.ErrorResponse;
import com.example.bank.demo.domain.exceptions.WithdrawalAmountBiggerThanBalanceException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.example.bank.demo.application.controller.handler.error.enumpackage.ErrorCode.WITHDRAWAL_EXCEEDING_OVERDRAFT;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(WithdrawalAmountBiggerThanBalanceException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(WithdrawalAmountBiggerThanBalanceException exception) {
        ErrorResponse errorResponse = new ErrorResponse(WITHDRAWAL_EXCEEDING_OVERDRAFT.getMessage(), WITHDRAWAL_EXCEEDING_OVERDRAFT.getCode());
        return new ResponseEntity<>(errorResponse, WITHDRAWAL_EXCEEDING_OVERDRAFT.getResponseStatus());
    }
}