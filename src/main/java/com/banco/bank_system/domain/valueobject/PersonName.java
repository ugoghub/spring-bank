package com.banco.bank_system.domain.valueobject;

import com.banco.bank_system.domain.exception.InvalidPersonNameException;

public record PersonName(String value) {

    public PersonName {

        if (value == null) {
            throw new InvalidPersonNameException("Nome não pode ser null");
        }

        value = value
                .trim()
                .replaceAll("\\s+", " ");

        if (!value.matches("^[\\p{L}' -]{2,}$")) {
            throw new InvalidPersonNameException(
                    "Nome inválido"
            );
        }
    }
}
