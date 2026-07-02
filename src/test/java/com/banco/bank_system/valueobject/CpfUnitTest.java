package com.banco.bank_system.valueobject;

import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.exception.InvalidCpfException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CpfUnitTest {

    // =========================
    // General
    // =========================

    @Test
    void shouldCreateValidCPF() {

        CPF cpf = createCPF("52998224725");

        assertEquals("52998224725", cpf.value());
    }

    @Test
    void shouldRemoveCPFFormatting() {

        CPF cpf = createCPF("529.982.247-25");

        assertEquals("52998224725", cpf.value());
    }

    // =========================
    // Validation
    // =========================

    @Test
    void shouldThrowExceptionWhenCPFIsNull() {

        assertThrows(
                InvalidCpfException.class,
                () -> createCPF(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenCPFContainsLetters() {

        assertThrows(
                InvalidCpfException.class,
                () -> createCPF("52998A24725")
        );
    }

    @Test
    void shouldThrowExceptionWhenCPFHasAllEqualDigits() {

        assertThrows(
                InvalidCpfException.class,
                () -> createCPF("11111111111")
        );
    }

    @Test
    void shouldThrowExceptionWhenCPFCheckDigitsAreInvalid() {

        assertThrows(
                InvalidCpfException.class,
                () -> createCPF("52998224724")
        );
    }

    @Test
    void shouldThrowExceptionWhenCPFHasInvalidSpaces() {

        assertThrows(
                InvalidCpfException.class,
                () -> createCPF(" 529.982 . 247-25 ")
        );
    }

    @Test
    void shouldThrowExceptionWhenCpfIsEmpty() {

        assertThrows(
                InvalidCpfException.class,
                () -> createCPF("")
        );
    }

    @Test
    void shouldThrowExceptionWhenCpfHasLessThanElevenDigits() {

        assertThrows(
                InvalidCpfException.class,
                () -> createCPF("123")
        );
    }

    @Test
    void shouldThrowExceptionWhenCpfHasMoreThanElevenDigits() {

        assertThrows(
                InvalidCpfException.class,
                () -> createCPF("123456789012")
        );
    }


    // =========================
    // Equality
    // =========================

    @Test
    void shouldBeEqualWhenCPFValuesAreEqual() {

        CPF first = createCPF("52998224725");

        CPF second = createCPF("529.982.247-25");

        assertEquals(first, second);
    }

    @Test
    void shouldHaveSameHashCodeWhenValuesAreEqual() {

        CPF first = createCPF("52998224725");

        CPF second = createCPF("529.982.247-25");

        assertEquals(first.hashCode(), second.hashCode());
    }

    // =========================
    // Helper
    // =========================

    private CPF createCPF(String cpf){
        return new CPF(cpf);
    }
}
