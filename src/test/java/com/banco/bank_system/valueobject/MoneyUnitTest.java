package com.banco.bank_system.valueobject;

import com.banco.bank_system.domain.exception.InvalidMoneyException;
import com.banco.bank_system.domain.valueobject.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyUnitTest {

    // =========================
    // General
    // =========================

    @Test
    void shouldIdentifyZero() {

        assertTrue(Money.ZERO.isZero());
    }

    @Test
    void shouldAddValuesCorrectly() {

        Money a = Money.of("10");
        Money b = Money.of("5");

        Money result = a.add(b);

        assertEquals(
                Money.of("15"),
                result
        );
    }

    @Test
    void shouldSubtractValuesCorrectly() {

        Money a = Money.of("20");
        Money b = Money.of("5");

        Money result = a.subtract(b);

        assertEquals(
                Money.of("15"),
                result
        );
    }

    @Test
    void shouldRoundToTwoDecimalPlaces() {

        Money money = Money.of("10.999");

        assertEquals(
                Money.of("11.00"),
                money
        );
    }

    @Test
    void shouldRoundHalfEvenValuesCorrectly() {

        Money money = Money.of("2.245");

        assertEquals(
                Money.of("2.24"),
                money
        );
    }

    @Test
    void shouldIdentifyNegativeValueOrZero() {

        Money money = Money.of("-10");

        assertTrue(
                money.isNegativeOrZero()
        );
    }

    @Test
    void shouldIdentifyZeroAsNegativeOrZero() {

        assertTrue(
                Money.ZERO.isNegativeOrZero()
        );
    }

    @Test
    void shouldReturnFalseForPositiveValue() {

        assertFalse(
                Money.of("10").isNegativeOrZero()
        );
    }

    @Test
    void shouldNegateValue() {

        Money money = Money.of("100");

        Money result = money.negate();

        assertEquals(
                Money.of("-100"),
                result
        );
    }

    @Test
    void shouldCompareValuesCorrectly() {

        Money a = Money.of("10");

        Money b = Money.of("20");

        assertTrue(b.isGreaterThan(a));
    }

    @Test
    void shouldMultiplyValuesCorrectly() {

        Money money = Money.of("10");

        Money result = money.multiplyByRate(
                new BigDecimal("2.5")
        );

        assertEquals(
                Money.of("25.00"),
                result
        );
    }

    @Test
    void shouldRoundMultiplicationResult() {

        Money result = Money.of("10")
                .multiplyByRate(
                        new BigDecimal("0.333")
                );

        assertEquals(
                Money.of("3.33"),
                result
        );
    }

    @Test
    void shouldBeImmutableAfterAddition() {

        Money original = Money.of("10");

        Money result = original.add(Money.of("5"));

        assertEquals(
                Money.of("10"),
                original
        );

        assertEquals(
                Money.of("15"),
                result
        );
    }

    @Test
    void shouldSubtractToNegativeValueCorrectly() {

        Money a = Money.of("5");

        Money b = Money.of("10");

        Money result =
                a.subtract(b);

        assertEquals(
                Money.of("-5.00"),
                result
        );
    }

    @Test
    void shouldCompareValuesCorrectlyUsingCompareTo() {

        Money smaller = Money.of("10");

        Money greater = Money.of("20");

        assertTrue(
                smaller.compareTo(greater) < 0
        );

        assertTrue(
                greater.compareTo(smaller) > 0
        );

        assertEquals(
                0,
                smaller.compareTo(Money.of("10.00"))
        );
    }

    // =========================
    // Validation
    // =========================

    @Test
    void shouldThrowExceptionIfValueIsNotANumber() {
        assertThrows(
                InvalidMoneyException.class,
                () -> Money.of("A%[]!()")
        );
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {

        assertThrows(
                InvalidMoneyException.class,
                () -> Money.of((BigDecimal) null)
        );
    }

    @Test
    void shouldThrowExceptionWhenTryingToAddNullValue() {

        Money money = Money.of("100");

        assertThrows(
                InvalidMoneyException.class,
                () -> money.add(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenTryingToSubtractNullValue() {

        Money money = Money.of("100");

        assertThrows(
                InvalidMoneyException.class,
                () -> money.subtract(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenTryingToMultiplyNullValue() {

        Money money = Money.of("100");

        assertThrows(
                InvalidMoneyException.class,
                () -> money.multiplyByRate(null)
        );
    }

    @Test
    void shouldThrowExceptionWhenTryingToCompareNullValue() {

        Money money = Money.of("100");

        assertThrows(
                InvalidMoneyException.class,
                () -> money.compareTo(null)
        );
    }

    // =========================
    // Equality
    // =========================

    @Test
    void shouldBeEqualWhenValuesAreEqual() {

        assertEquals(
                Money.of("10"),
                Money.of(new BigDecimal("10.00"))
        );
    }

    @Test
    void shouldNotBeEqualWhenValuesAreNotEqual() {

        assertNotEquals(
                Money.of("10"),
                Money.of("20")
        );
    }

    @Test
    void shouldHaveSameHashCodeWhenValuesAreEqual() {

        assertEquals(
                Money.of("10").hashCode(),
                Money.of("10.00").hashCode()
        );
    }
}
