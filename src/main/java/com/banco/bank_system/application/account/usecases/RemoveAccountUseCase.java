package com.banco.bank_system.application.account.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.util.AccountFinder;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.application.exception.CannotRemoveAccountException;
import com.banco.bank_system.application.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RemoveAccountUseCase {

    private final AccountRepositoryPort accountRepository;
    private final AccountFinder accountFinder;

    public RemoveAccountUseCase(AccountRepositoryPort accountRepository,
                                   AccountFinder accountFinder) {
        this.accountRepository = accountRepository;
        this.accountFinder = accountFinder;
    }

    public void execute(AccountIdentity accountIdentity){

        Account account = accountFinder.byIdentity(accountIdentity);

        if (!account.isRemovable()) {
            throw new CannotRemoveAccountException(
                    "Conta não pode ser excluída com saldo diferente de zero"
            );
        }

        accountRepository.delete(account.getId());
    }
}
