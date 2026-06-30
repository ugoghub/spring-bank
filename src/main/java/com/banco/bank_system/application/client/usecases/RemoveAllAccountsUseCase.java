package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.application.exception.CannotRemoveAccountException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RemoveAllAccountsUseCase {

    private final AccountRepositoryPort accountRepository;

    public RemoveAllAccountsUseCase(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void execute(UUID clientId) {

        List<Account> accounts = accountRepository.getAccountsByClient(clientId);

        for (Account account : accounts) {
            if (!account.canBeRemoved()) {
                throw new CannotRemoveAccountException(
                        "Não é possível remover as contas. A conta " +
                                account.getAccountIdentity() + " possui saldo ativo."
                );
            }
        }

        accountRepository.removeClientAccounts(clientId);
    }
}