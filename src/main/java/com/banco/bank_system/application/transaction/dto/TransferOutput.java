package com.banco.bank_system.application.transaction.dto;

import com.banco.bank_system.domain.valueobject.Money;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransferOutput(
        UUID operationId,
        UUID source,
        UUID destination,
        Money amount,
        LocalDateTime transactionDate
) {}
