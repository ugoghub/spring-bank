package com.banco.bank_system.application.account.port;

import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.AccountIdentity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryPort {

    boolean existsByAccountIdentity(AccountIdentity accountIdentity);

    void save(Account account);

    List<Account> getAccountsByClient(UUID clientId);

    Optional<Account> getAccountByAccountIdentity(AccountIdentity accountIdentity);

    void removeAccount(UUID accountId);

    void removeClientAccounts(UUID clientId);
}