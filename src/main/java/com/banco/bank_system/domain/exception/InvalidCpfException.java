package com.banco.bank_system.domain.exception;

public class InvalidCpfException extends DomainException {
    public InvalidCpfException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "INVALID_CPF";
    }
}
