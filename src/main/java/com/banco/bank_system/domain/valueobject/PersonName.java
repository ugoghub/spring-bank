package com.banco.bank_system.domain.valueobject;

public record PersonName(String value) {

    public PersonName {

        if (value == null) {
            throw new IllegalArgumentException("Nome não pode ser null");
        }

        value = value
                .trim()
                .replaceAll("\\s+", " ");

        if (!value.matches("^[\\p{L}' -]{2,}$")) {
            throw new IllegalArgumentException(
                    "Nome inválido"
            );
        }
    }
}
