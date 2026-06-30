package com.banco.bank_system.domain.exception;

public class InvalidClientChangeException extends DomainException {
    public InvalidClientChangeException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "INVALID_CLIENT_CHANGE";
    }
}
