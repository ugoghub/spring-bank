package com.banco.bank_system.domain.exception;

public class InvalidPersonNameException extends DomainException {
    public InvalidPersonNameException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "INVALID_NAME";
    }
}
