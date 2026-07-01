package com.banco.bank_system.application.transaction.dto;

import com.banco.bank_system.domain.valueobject.Money;

import java.time.LocalDateTime;
import java.util.UUID;

public record DepositOutput(
        UUID accountId,
        Money depositedAmount,
        Money newBalance,
        UUID transactionId,
        LocalDateTime transactionDate
) {}
