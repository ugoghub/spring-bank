package com.banco.bank_system.application.exception;

public class EmailAlreadyExistsException extends ResourceAlreadyExistsException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "EMAIL_ALREADY_EXISTS";
    }
}
