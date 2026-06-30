package com.banco.bank_system.application.exception;

public abstract class ResourceNotFoundException extends ApplicationException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
