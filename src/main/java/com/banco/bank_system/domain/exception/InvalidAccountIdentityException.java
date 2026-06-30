package com.banco.bank_system.domain.exception;

public class InvalidAccountIdentityException extends DomainException {
    public InvalidAccountIdentityException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "INVALID_ACCOUNT_IDENTITY";
    }
}
