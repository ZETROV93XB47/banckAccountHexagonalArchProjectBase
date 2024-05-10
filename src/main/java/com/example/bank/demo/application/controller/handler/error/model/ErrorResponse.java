package com.example.bank.demo.application.controller.handler.error.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class ErrorResponse {
    private final String message;
    private final int code;
}