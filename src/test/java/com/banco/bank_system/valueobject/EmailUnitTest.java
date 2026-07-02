package com.banco.bank_system.valueobject;

import com.banco.bank_system.domain.exception.InvalidEmailException;
import com.banco.bank_system.domain.valueobject.Email;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailUnitTest {

    // =========================
    // General
    // =========================

    @Test
    void shouldCreateValidEmail() {

        Email email =
                createEmail("teste@gmail.com");

        assertEquals(
                "teste@gmail.com",
                email.value()
        );
    }

    @Test
    void shouldNormalizeEmail() {

        Email email =
                createEmail("  TESTE@GMAIL.COM ");

        assertEquals(
                "teste@gmail.com",
                email.value()
        );
    }

    @Test
    void shouldNotBeEqualWhenEmailsAreDifferent() {

        assertNotEquals(
                createEmail("a@gmail.com"),
                createEmail("b@gmail.com")
        );
    }

    @Test
    void shouldBeEqualAfterNormalization() {

        Email first =
                createEmail("TESTE@GMAIL.COM");

        Email second =
                createEmail("teste@gmail.com");

        assertEquals(first, second);
    }

    @Test
    void shouldHaveSameHashCodeAfterNormalization() {

        Email first = createEmail("TESTE@GMAIL.COM");
        Email second = createEmail("teste@gmail.com");

        assertEquals(first.hashCode(), second.hashCode());
    }

    // =========================
    // Validation
    // =========================

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {

        assertThrows(
                InvalidEmailException.class,
                () -> createEmail("teste.com")
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {

        assertThrows(
                InvalidEmailException.class,
                () -> createEmail(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailIsEmpty() {

        assertThrows(
                InvalidEmailException.class,
                () -> createEmail("")
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailHasNoDomain() {

        assertThrows(
                InvalidEmailException.class,
                () -> createEmail("teste@")
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailHasNoUser() {

        assertThrows(
                InvalidEmailException.class,
                () -> createEmail("@gmail.com")
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailHasMultipleAtSymbols() {

        assertThrows(
                InvalidEmailException.class,
                () -> createEmail("teste@@gmail.com")
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailDomainIsInvalid() {

        assertThrows(
                InvalidEmailException.class,
                () -> createEmail("teste@gmail")
        );
    }

    @Test
    void shouldThrowExceptionWhenEmailContainsInternalSpaces() {

        assertThrows(
                InvalidEmailException.class,
                () -> createEmail("tes te@gmail.com")
        );
    }

    // =========================
    // Helper
    // =========================

    private Email createEmail(String email){
        return new Email(email);
    }
}
