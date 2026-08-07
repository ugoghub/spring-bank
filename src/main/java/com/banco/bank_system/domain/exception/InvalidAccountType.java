package com.banco.bank_system.domain.exception;

public class InvalidAccountType extends DomainException{
    public InvalidAccountType() {
        super("Tipo de Conta Inválida");
    }

    @Override
    public String getCode() {
        return "INVALID_ACCOUNT_TYPE";
    }
}
