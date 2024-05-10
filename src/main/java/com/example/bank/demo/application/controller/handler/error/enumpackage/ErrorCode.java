package com.example.bank.demo.application.controller.handler.error.enumpackage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    APPLICATION_ERROR(1, "Une erreur est survenue", HttpStatus.INTERNAL_SERVER_ERROR, ErrorCodeType.TECHNICAL),
    WITHDRAWAL_EXCEEDING_OVERDRAFT(2, "Le montant que vous tentez de retirer est supérieur au montant autorisé pour votre compte", HttpStatus.FORBIDDEN, ErrorCodeType.FUNCTIONAL);

    private final int code;
    private final String message;
    private final HttpStatus responseStatus;
    private final ErrorCodeType errorCodeType;
}
