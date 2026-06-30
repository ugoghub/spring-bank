package com.banco.bank_system.application.exception;

public abstract class ApplicationException extends RuntimeException{
    public ApplicationException(String message) {
        super(message);
    }

    public abstract String getCode();
}
