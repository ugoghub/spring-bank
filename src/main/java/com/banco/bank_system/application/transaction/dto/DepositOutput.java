package com.banco.bank_system.application.transaction.dto;

import com.banco.bank_system.domain.valueobject.AccountId;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.domain.valueobject.TransactionId;

import java.time.LocalDateTime;
import java.util.UUID;

public record DepositOutput(
        AccountId accountId,
        Money depositedAmount,
        Money newBalance,
        TransactionId transactionId,
        LocalDateTime transactionDate
) {}
