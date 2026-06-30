package com.banco.bank_system.domain.exception;

public abstract class DomainException extends RuntimeException{
    public DomainException(String message) {
        super(message);
    }

    public abstract String getCode();
}
