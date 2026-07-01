package com.banco.bank_system.infrastructure.database.adapters;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.ClientId;
import com.banco.bank_system.infrastructure.database.entities.AccountEntity;
import com.banco.bank_system.infrastructure.database.sql.JpaAccountRepository;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AccountRepositoryAdapter
        implements AccountRepositoryPort {


    private final JpaAccountRepository repository;

    public AccountRepositoryAdapter(
            JpaAccountRepository repository
    ) {

        this.repository = repository;
    }

    @Override
    public void save(Account account) {

        repository.save(
                AccountEntity.fromDomain(account)
        );
    }


    @Override
    public List<Account> getAccountsByClient(ClientId clientId) {

        return repository
                .findByClientId(clientId.id())
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
    public void delete(UUID accountId) {

        repository.deleteById(accountId);
    }


    @Override
    public void removeClientAccounts(ClientId clientId) {

        repository.deleteByClientId(clientId.id());
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