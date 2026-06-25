package com.banco.bank_system.application.account.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.AccountIdentity;

import java.util.List;
import java.util.UUID;

public class GetClientAccountsUseCase {

    private final AccountRepositoryPort accountRepository;

    public GetClientAccountsUseCase(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<AccountIdentity> execute(UUID clientId){

        return accountRepository
                .getAccountsByClient(clientId)
                .stream()
                .map(Account::getAccountIdentity)
                .toList();
    }
}
