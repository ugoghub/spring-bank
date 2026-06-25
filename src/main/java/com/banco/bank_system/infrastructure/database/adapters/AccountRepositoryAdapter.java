package com.banco.bank_system.infrastructure.database.adapters;


import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.infrastructure.database.entities.AccountEntity;
import com.banco.bank_system.infrastructure.database.sql.SpringDataAccountRepository;

import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Optional;
import java.util.UUID;



@Component
public class AccountRepositoryAdapter
        implements AccountRepositoryPort {


    private final SpringDataAccountRepository repository;



    public AccountRepositoryAdapter(
            SpringDataAccountRepository repository
    ) {

        this.repository = repository;
    }



    @Override
    public void save(Account account) {


        AccountEntity entity =
                AccountEntity.fromDomain(account);


        repository.save(entity);

    }



    @Override
    public List<Account> getAccountsByClient(UUID clientId) {


        return repository
                .findByClientId(clientId)
                .stream()
                .map(AccountEntity::toDomain)
                .toList();

    }



    @Override
    public Optional<Account> getAccountByAccountIdentity(
            AccountIdentity accountIdentity
    ) {


        return repository
                .findByAccountNumberAndBranch(
                        accountIdentity.accountNumber(),
                        accountIdentity.branch()
                )
                .map(AccountEntity::toDomain);

    }



    @Override
    public void removeAccount(UUID accountId) {

        repository.deleteById(accountId);

    }



    @Override
    public void removeClientAccounts(UUID clientId) {

        repository.deleteByClientId(clientId);

    }



    @Override
    public boolean existsByAccountIdentity(
            AccountIdentity accountIdentity
    ) {


        return repository.existsByAccountNumberAndBranch(
                accountIdentity.accountNumber(),
                accountIdentity.branch()
        );

    }

}