package com.banco.bank_system.application.account.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.util.AccountFinder;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RemoveAccountUseCase {

    private final AccountRepositoryPort accountRepository;
    private final AccountFinder accountFinder;

    public RemoveAccountUseCase(AccountRepositoryPort accountRepository,
                                   AccountFinder accountFinder) {
        this.accountRepository = accountRepository;
        this.accountFinder = accountFinder;
    }

    @Transactional
    public void execute(AccountIdentity accountIdentity){

        Account account = accountFinder.byIdentity(accountIdentity);

        account.validateRemoval();

        accountRepository.delete(account.getId());
    }
}
