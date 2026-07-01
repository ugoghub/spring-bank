package com.banco.bank_system.application.transaction.dto;

import com.banco.bank_system.domain.enums.TransactionType;
import com.banco.bank_system.domain.valueobject.Money;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDTO(
        UUID id,
        UUID operationId,
        TransactionType type,
        Money amount,
        UUID source,
        UUID destination,
        LocalDateTime dateTime
) {
}
