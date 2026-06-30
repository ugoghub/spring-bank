package com.banco.bank_system.application.exception;

public class AccountNotFoundException extends ResourceNotFoundException {
    public AccountNotFoundException() {
        super("Conta não encontrada");
    }

    @Override
    public String getCode() {
        return "ACCOUNT_NOT_FOUND";
    }
}
