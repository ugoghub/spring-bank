package com.banco.bank_system.domain.enums;

public enum TransactionType {
    DEPOSIT("DEPOSIT"),
    INTEREST("INTEREST"),
    TRANSFER_SENT("TRANSFER_SENT"),
    TRANSFER_RECEIVED("TRANSFER_RECEIVED"),
    WITHDRAW("WITHDRAW");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
