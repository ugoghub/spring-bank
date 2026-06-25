package com.banco.bank_system.application.account.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.SavingsAccount;
import com.banco.bank_system.domain.enums.AccountType;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.AccountIdentityFactory;

import java.time.Clock;

import java.util.UUID;

public class CreateAccountUseCase {

    private final AccountRepositoryPort accountRepository;
    private final Clock clock;


    public CreateAccountUseCase(AccountRepositoryPort accountRepository, Clock clock) {
        this.accountRepository = accountRepository;
        this.clock = clock;
    }

    public AccountIdentity execute(UUID clientId, AccountType type){
        AccountIdentity accountIdentity;

        do {

            accountIdentity = AccountIdentityFactory.generate();

        } while (accountRepository.existsByAccountIdentity(accountIdentity)); // garante unicidade do AccountIdentity

        final Account account =
                switch (type) {
                    case CHECKING ->
                            new CheckingAccount(
                                    clientId,
                                    accountIdentity,
                                    clock
                            );

                    case SAVINGS ->
                            new SavingsAccount(
                                    clientId,
                                    accountIdentity,
                                    clock
                            );
                };

        accountRepository.save(account);

        return account.getAccountIdentity();
    }
}
