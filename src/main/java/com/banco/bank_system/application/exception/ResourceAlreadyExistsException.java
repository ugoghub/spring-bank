package com.banco.bank_system.application.exception;

public abstract class ResourceAlreadyExistsException extends ApplicationException{
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
