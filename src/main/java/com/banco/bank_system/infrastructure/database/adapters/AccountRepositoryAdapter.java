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
public class AccountRepositoryAdapter implements AccountRepositoryPort {

    private final SpringDataAccountRepository springDataRepository;

    public AccountRepositoryAdapter(SpringDataAccountRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public void save(Account account) {
        // 1. Converte a Entidade do Domínio para a Entidade do Banco (JPA)
        AccountEntity entity = AccountEntity.fromDomain(account);

        // 2. Salva usando o poder do Spring Data por baixo dos panos
        springDataRepository.save(entity);
    }

    @Override
    public List<Account> getAccountsByClient(UUID clientId) {
        return List.of();
    }

    @Override
    public Optional<Account> getAccountByAccountIdentity(AccountIdentity accountIdentity) {
        return Optional.empty();
    }

    @Override
    public void removeAccount(UUID accountId) {

    }

    @Override
    public void removeClientAccounts(UUID clientId) {

    }

    @Override
    public boolean existsByAccountIdentity(AccountIdentity accountIdentity) {
       return true;
    }
}