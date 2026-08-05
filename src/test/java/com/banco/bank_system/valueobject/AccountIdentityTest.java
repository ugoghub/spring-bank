package com.banco.bank_system.valueobject;

import com.banco.bank_system.domain.exception.InvalidAccountIdentityException;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountIdentityTest {

    private static final AccountIdentity accountIdentity =
            new AccountIdentity(
                    "01",
                    "123456-1"
            );


    // =========================
    // General
    // =========================

    @Test
    void shouldCreateValidIdentity() {

        AccountIdentity identity =
                accountIdentity;

        assertEquals("01", identity.branch());

        assertEquals(
                "123456-1",
                identity.accountNumber()
        );
    }

    @Test
    void shouldAllowLeadingZeros() {

        AccountIdentity identity =
                createIdentity(
                        "00",
                        "000001-1"
                );

        assertEquals("00", identity.branch());

        assertEquals(
                "000001-1",
                identity.accountNumber()
        );
    }


    // =========================
    // Validation
    // =========================

    @Test
    void shouldThrowExceptionWhenBranchIsInvalid() {

        assertThrows(
                InvalidAccountIdentityException.class,
                () -> createIdentity(
                        "1",
                        "123456-1"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenBranchHasMoreThanTwoDigits() {
        assertThrows(
                InvalidAccountIdentityException.class,
                () -> createIdentity(
                        "123",
                        "123456-1"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenBranchContainsLetters() {

        assertThrows(
                InvalidAccountIdentityException.class,
                () -> createIdentity(
                        "AA",
                        "123456-1"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenBranchIsNull() {

        assertThrows(
                InvalidAccountIdentityException.class,
                () -> createIdentity(
                        null,
                        "123456-1"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenBranchIsEmpty() {

        assertThrows(
                InvalidAccountIdentityException.class,
                () -> createIdentity(
                        "",
                        "123456-1"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenDigitIsInvalid() {

        assertThrows(
                InvalidAccountIdentityException.class,
                () -> createIdentity(
                        "01",
                        "123456-0"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenAccountNumberHasWrongFormat() {

        assertThrows(
                InvalidAccountIdentityException.class,
                () -> createIdentity(
                        "01",
                        "1234561"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenAccountNumberIsTooShort() {

        assertThrows(
                InvalidAccountIdentityException.class,
                () -> createIdentity(
                        "01",
                        "12345-1"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenAccountNumberIsTooLong() {

        assertThrows(
                InvalidAccountIdentityException.class,
                () -> createIdentity(
                        "01",
                        "1234567-1"
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenAccountNumberIsNull() {

        assertThrows(
                InvalidAccountIdentityException.class,
                () -> createIdentity(
                        "01",
                        null
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenAccountNumberContainsLetters() {

        assertThrows(
                InvalidAccountIdentityException.class,
                () -> createIdentity(
                        "01",
                        "abcdeF-1"
                )
        );
    }

    // =========================
    // Equality
    // =========================

    @Test
    void shouldBeEqualWhenValuesAreEqual() {

        AccountIdentity sameAccountIdentity =
                createIdentity("01","123456-1");

        assertEquals(accountIdentity,sameAccountIdentity);
    }


    @Test
    void shouldHaveSameHashCodeWhenValuesAreEqual() {

        AccountIdentity sameAccountIdentity =
                createIdentity("01","123456-1");

        assertEquals(
                accountIdentity.hashCode(),
                sameAccountIdentity.hashCode()
        );
    }

    // =========================
    // Helper
    // =========================

    private AccountIdentity createIdentity(String branch, String accountNumber){
        return new AccountIdentity(branch, accountNumber);
    }
}
