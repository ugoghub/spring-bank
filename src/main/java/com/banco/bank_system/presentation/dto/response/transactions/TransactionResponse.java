package com.banco.bank_system.presentation.dto.response.transactions;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID operationId,
        String type,
        String amount,
        UUID source,
        UUID destination,
        String dateTime
) {
}
