package com.banco.bank_system.domain.exception;

public class InvalidTransactionIdException extends DomainException{
    public InvalidTransactionIdException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "INVALID_TRANSACTION_ID";
    }
}
