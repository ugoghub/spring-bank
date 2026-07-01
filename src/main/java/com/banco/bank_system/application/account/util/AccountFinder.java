package com.banco.bank_system.application.account.util;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.exception.AccountNotFoundException;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import org.springframework.stereotype.Component;

@Component
public class AccountFinder {

    private final AccountRepositoryPort accountRepository;

    public AccountFinder(AccountRepositoryPort accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account byIdentity(AccountIdentity accountIdentity){
        return accountRepository.
                getAccountByAccountIdentity(accountIdentity)
                .orElseThrow(AccountNotFoundException::new);
    }
}
