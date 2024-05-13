package com.example.bank.demo.application.controller.handler.error.enumpackage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    APPLICATION_ERROR("Une erreur est survenue", HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodeType.TECHNICAL),
    WITHDRAWAL_EXCEEDING_OVERDRAFT("Le montant que vous tentez de retirer est supérieur au montant autorisé pour votre compte", HttpStatus.FORBIDDEN, ErrorCodeType.FUNCTIONAL),
    ACCOUNT_NOT_FOUND("Account not found", HttpStatus.NOT_FOUND, ErrorCodeType.FUNCTIONAL),
    DEPOSIT_LIMIT_REACHED("Your Deposit value exceed the deposit limit on your account ", HttpStatus.FORBIDDEN, ErrorCodeType.FUNCTIONAL);

    private final String message;
    private final HttpStatus responseStatus;
    private final ErrorCodeType errorCodeType;
}
