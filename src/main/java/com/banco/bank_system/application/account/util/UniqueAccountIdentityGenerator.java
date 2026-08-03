package com.banco.bank_system.application.account.util;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.AccountIdentityFactory;
import org.springframework.stereotype.Component;

@Component
public class UniqueAccountIdentityGenerator {

    private final AccountRepositoryPort accountRepository;

    public UniqueAccountIdentityGenerator(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountIdentity generate() {

        AccountIdentity identity;

        do {
            identity = AccountIdentityFactory.generate();
        } while (accountRepository.existsByAccountIdentity(identity));

        return identity;
    }

}