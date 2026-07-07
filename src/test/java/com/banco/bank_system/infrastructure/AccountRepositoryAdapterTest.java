package com.banco.bank_system.infrastructure;

import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.entities.SavingsAccount;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.entities.helper.AccountFactory;
import com.banco.bank_system.infrastructure.database.adapters.AccountRepositoryAdapter;
import com.banco.bank_system.infrastructure.database.entities.ClientEntity;
import com.banco.bank_system.infrastructure.database.sql.JpaAccountRepository;
import com.banco.bank_system.infrastructure.database.sql.JpaClientRepository;
import com.banco.bank_system.useCase.clientUseCaseTests.helper.ClientFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(AccountRepositoryAdapter.class)
class AccountRepositoryAdapterTest {

    @Autowired
    private AccountRepositoryAdapter adapter;

    @Autowired
    private JpaAccountRepository repository;

    @Autowired
    private JpaClientRepository clientRepository;

    private Clock clock;

    @BeforeEach
    void setup() {
        clock = Clock.fixed(
                Instant.parse("2026-01-10T10:00:00Z"),
                ZoneOffset.UTC
        );
    }

    @Test
    void shouldSaveAccount() {

        CheckingAccount account =
                AccountFactory.checking(clock);

        adapter.save(account);

        assertEquals(1, repository.count());
    }

    @Test
    void shouldFindAccountByIdentity() {

        CheckingAccount account =
                AccountFactory.checking(clock);

        adapter.save(account);

        Optional<Account> result =
                adapter.getAccountByAccountIdentity(
                        account.getAccountIdentity());

        assertTrue(result.isPresent());

        assertEquals(
                account.getId(),
                result.get().getId()
        );
    }

    @Test
    void shouldReturnEmptyWhenAccountDoesNotExist() {

        AccountIdentity identity =
                new AccountIdentity("01", "123456-1");

        Optional<Account> result =
                adapter.getAccountByAccountIdentity(identity);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnAccountsByClient() {

        Client client = ClientFactory.create();

        clientRepository.save(ClientEntity.fromDomain(client));

        CheckingAccount checking =
                AccountFactory.checking(client.getId(), clock);

        SavingsAccount savings =
                AccountFactory.savings(client.getId(), clock);

        adapter.save(checking);
        adapter.save(savings);

        List<Account> accounts =
                adapter.getAccountsByClient(client.getId());

        assertEquals(2, accounts.size());

        assertTrue(accounts.stream()
                .anyMatch(a -> a.getId().equals(checking.getId())));

        assertTrue(accounts.stream()
                .anyMatch(a -> a.getId().equals(savings.getId())));
    }

    @Test
    void shouldDeleteAccount() {

        Client client = ClientFactory.create();

        clientRepository.save(ClientEntity.fromDomain(client));

        CheckingAccount account =
                AccountFactory.checking(client.getId(), clock);

        adapter.save(account);

        adapter.delete(account.getId());

        assertFalse(
                repository.existsById(account.getId().id())
        );
    }

    @Test
    void shouldCheckExistsByAccountIdentity() {

        Client client = ClientFactory.create();

        clientRepository.save(ClientEntity.fromDomain(client));

        CheckingAccount account =
                AccountFactory.checking(client.getId(), clock);

        adapter.save(account);

        assertTrue(
                adapter.existsByAccountIdentity(
                        account.getAccountIdentity()
                )
        );
    }

    @Test
    void shouldReturnFalseWhenIdentityDoesNotExist() {

        AccountIdentity identity =
                new AccountIdentity("01", "123456-1");

        assertFalse(
                adapter.existsByAccountIdentity(identity)
        );
    }

    @Test
    void shouldRemoveAllAccountsFromClient() {

        Client client = ClientFactory.create();

        clientRepository.save(ClientEntity.fromDomain(client));

        adapter.save(
                AccountFactory.checking(client.getId(), clock)
        );

        adapter.save(
                AccountFactory.savings(client.getId(), clock)
        );

        adapter.removeClientAccounts(client.getId());

        List<Account> accounts =
                adapter.getAccountsByClient(client.getId());

        assertTrue(accounts.isEmpty());

        assertEquals(0, repository.count());
    }
}
