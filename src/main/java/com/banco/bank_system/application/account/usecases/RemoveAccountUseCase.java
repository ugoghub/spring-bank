package com.banco.bank_system.application.account.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.AccountIdentity;

public class RemoveAccountUseCase {

    private final AccountRepositoryPort accountRepository;

    public RemoveAccountUseCase(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void execute(AccountIdentity accountIdentity){

        Account account = accountRepository.
                getAccountByAccountIdentity(accountIdentity)
                .orElseThrow(() -> new IllegalArgumentException("accountidentity invalida"));

        if (!account.canBeRemoved()) {
            throw new IllegalArgumentException("Conta não pode ser excluída com saldo diferente de zero");
        }

        accountRepository.removeAccount(account.getId());
    }
}
