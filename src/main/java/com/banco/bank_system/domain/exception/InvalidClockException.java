package com.banco.bank_system.domain.exception;

public class InvalidClockException extends DomainException{
    public InvalidClockException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "INVALID_CLOCK";
    }
}
