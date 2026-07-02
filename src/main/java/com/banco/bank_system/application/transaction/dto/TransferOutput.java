package com.banco.bank_system.application.transaction.dto;

import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.domain.valueobject.OperationId;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransferOutput(
        OperationId operationId,
        AccountIdentity source,
        AccountIdentity destination,
        Money amount,
        LocalDateTime transactionDate
) {}
