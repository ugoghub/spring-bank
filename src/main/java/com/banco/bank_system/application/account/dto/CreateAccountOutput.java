package com.banco.bank_system.application.account.dto;

import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateAccountOutput(
        UUID id,
        UUID clientId,
        AccountIdentity accountIdentity,
        LocalDateTime creationTime,
        Money balance
) {
}
