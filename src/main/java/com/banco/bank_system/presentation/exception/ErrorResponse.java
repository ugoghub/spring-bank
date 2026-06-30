package com.banco.bank_system.presentation.exception;

public record ErrorResponse(
        String timestamp,
        int status,
        String error,
        String code,
        String message,
        String path
) {
}
