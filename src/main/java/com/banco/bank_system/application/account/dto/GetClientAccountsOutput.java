package com.banco.bank_system.application.account.dto;

import com.banco.bank_system.domain.valueobject.AccountIdentity;

import java.util.List;

public record GetClientAccountsOutput(List<AccountIdentity> accountIdentities) {
}
