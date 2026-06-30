package com.banco.bank_system.application.exception;

public class ClientNotFoundException extends ResourceNotFoundException {
    public ClientNotFoundException() {
        super("Cliente não encontrado");
    }

    @Override
    public String getCode() {
        return "CLIENT_NOT_FOUND";
    }
}
