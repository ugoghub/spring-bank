package com.banco.bank_system.application.account.dto;

import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.ClientId;
import com.banco.bank_system.domain.valueobject.Money;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetClientAccountOutput(
        UUID id,
        ClientId clientId,
        AccountIdentity accountIdentity,
        LocalDateTime creationTime,
        Money balance
) {
}
