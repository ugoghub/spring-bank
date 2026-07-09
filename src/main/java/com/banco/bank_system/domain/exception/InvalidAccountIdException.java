package com.banco.bank_system.domain.exception;

public class InvalidAccountIdException extends DomainException{
    public InvalidAccountIdException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "INVALID_ACCOUNT_ID";
    }
}
