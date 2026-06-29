package com.banco.bank_system.presentation.dto.request;

import java.util.UUID;

public record CreateAccountRequest(UUID clientId, String AccountType){
}
