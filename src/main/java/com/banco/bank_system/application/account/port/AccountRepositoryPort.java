package com.banco.bank_system.application.account.port;

import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.ClientId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryPort {

    boolean existsByAccountIdentity(AccountIdentity accountIdentity);

    void save(Account account);

    List<Account> getAccountsByClient(ClientId clientId);

    Optional<Account> getAccountByAccountIdentity(AccountIdentity accountIdentity);

    void delete(UUID accountId);

    void removeClientAccounts(ClientId clientId);
}