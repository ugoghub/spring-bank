package com.banco.bank_system.domain.exception;

public class InvalidMoneyException extends DomainException {
    public InvalidMoneyException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "INVALID_MONEY";
    }
}
