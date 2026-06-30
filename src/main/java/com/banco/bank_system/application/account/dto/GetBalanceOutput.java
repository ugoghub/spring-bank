package com.banco.bank_system.application.account.dto;

import com.banco.bank_system.domain.valueobject.Money;

public record GetBalanceOutput(Money balance) {
}
