package com.banco.bank_system.domain.exception;

public class InvalidEmailException extends DomainException {
    public InvalidEmailException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "INVALID_EMAIL";
    }
}
