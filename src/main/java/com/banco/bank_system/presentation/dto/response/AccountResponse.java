package com.banco.bank_system.presentation.dto.response;

public record AccountResponse(
        String branch,
        String accountNumber,
        String id,
        String clientId,
        String balance,
        String creationTime
){
}
