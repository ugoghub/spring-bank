package com.banco.bank_system.domain.exception;

public class InsufficientBalanceException extends DomainException {
    public InsufficientBalanceException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "INSUFFICIENT_BALANCE";
    }
}
