package com.banco.bank_system.domain.enums;

public enum AccountType {
    CHECKING("Conta Corrente"),
    SAVINGS("Conta Poupança");

    private final String description;

    AccountType(String description) {
        this.description = description;
    }

    public static AccountType from(String value) {
        try {
            return AccountType.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Tipo de conta inválido");
        }
    }

    @Override
    public String toString() {
        return description;
    }
}
