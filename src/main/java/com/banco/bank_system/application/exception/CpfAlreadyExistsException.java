package com.banco.bank_system.application.exception;

public class CpfAlreadyExistsException extends ResourceAlreadyExistsException {
    public CpfAlreadyExistsException() {
        super("CPF já cadastrado");
    }

    @Override
    public String getCode() {
        return "CPF_ALREADY_EXISTS";
    }
}
