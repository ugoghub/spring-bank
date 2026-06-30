package com.banco.bank_system.application.exception;

public class InvalidTransferException extends ApplicationException {
    public InvalidTransferException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "INVALID_TRANSFER";
    }
}
