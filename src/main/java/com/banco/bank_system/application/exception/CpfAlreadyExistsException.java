package com.banco.bank_system.application.exception;

public class CpfAlreadyExistsException extends ResourceAlreadyExistsException {
    public CpfAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "CPF_ALREADY_EXISTS";
    }
}
