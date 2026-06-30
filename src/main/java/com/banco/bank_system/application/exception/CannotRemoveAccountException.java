package com.banco.bank_system.application.exception;

public class CannotRemoveAccountException extends ApplicationException {
    public CannotRemoveAccountException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "CANNOT_REMOVE_ACCOUNT";
    }
}
