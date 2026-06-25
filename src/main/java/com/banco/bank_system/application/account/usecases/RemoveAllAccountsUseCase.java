package com.banco.bank_system.application.account.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import java.util.List;
import java.util.UUID;

class RemoveAllAccountsUseCase {

    private final AccountRepositoryPort accountRepository;

    public RemoveAllAccountsUseCase(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void execute(UUID clientId) {

        List<Account> accounts = accountRepository.getAccountsByClient(clientId);

        for (Account account : accounts) {
            if (!account.canBeRemoved()) { // Seu método rico do domínio!
                throw new IllegalStateException(
                        "Não é possível remover as contas. A conta " +
                                account.getAccountIdentity() + " possui saldo ativo."
                );
            }
        }

        accountRepository.removeClientAccounts(clientId);
    }
}