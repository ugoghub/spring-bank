package com.banco.bank_system.presentation.util;

import com.banco.bank_system.domain.valueobject.Money;

import java.text.NumberFormat;
import java.util.Locale;

public final class CurrencyFormatter {

    private static final NumberFormat FORMAT =
            NumberFormat.getCurrencyInstance(
                    Locale.of("pt", "BR")
            );

    public static String format(Money money) {
        return FORMAT.format(money.value());
    }
}
