package com.banco.bank_system.entities;

import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.exception.InvalidCpfException;
import com.banco.bank_system.domain.exception.InvalidEmailException;
import com.banco.bank_system.domain.exception.InvalidPersonNameException;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClientTest {

    @Test
    void shouldThrowExceptionWhenCreatingClientWithNullName() {

        assertThrows(
                InvalidPersonNameException.class,
                () -> Client.create(null, new CPF("52998224725"), new Email("pedro@gmail.com"))
        );
    }

    @Test
    void shouldThrowExceptionWhenCreatingClientWithNullCPF() {

        assertThrows(
                InvalidCpfException.class,
                () -> Client.create(new PersonName("pedro"), null, new Email("pedro@gmail.com"))
        );
    }

    @Test
    void shouldThrowExceptionWhenCreatingClientWithNullEmail() {

        assertThrows(
                InvalidEmailException.class,
                () -> Client.create(new PersonName("pedro"), new CPF("52998224725"), null)
        );
    }
}