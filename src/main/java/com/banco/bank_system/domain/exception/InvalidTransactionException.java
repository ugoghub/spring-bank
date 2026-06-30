package com.banco.bank_system.domain.exception;

public class InvalidTransactionException extends DomainException {
    public InvalidTransactionException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "INVALID_TRANSACTION";
    }
}
