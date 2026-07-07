package com.banco.bank_system.infrastructure;

import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.enums.TransactionType;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.entities.helper.AccountFactory;
import com.banco.bank_system.infrastructure.database.adapters.AccountRepositoryAdapter;
import com.banco.bank_system.infrastructure.database.adapters.TransactionRepositoryAdapter;
import com.banco.bank_system.infrastructure.database.entities.ClientEntity;
import com.banco.bank_system.infrastructure.database.sql.JpaAccountRepository;
import com.banco.bank_system.infrastructure.database.sql.JpaClientRepository;
import com.banco.bank_system.infrastructure.database.sql.JpaTransactionRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import({
        TransactionRepositoryAdapter.class,
        AccountRepositoryAdapter.class
})
class TransactionRepositoryAdapterTest {

    @Autowired
    private TransactionRepositoryAdapter adapter;

    @Autowired
    private AccountRepositoryAdapter accountRepositoryAdapter;

    @Autowired
    private JpaTransactionRepository repository;

    @Autowired
    private JpaAccountRepository accountRepository;

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
    void shouldSaveTransaction() {

        Client client = ClientFactory.create();
        clientRepository.save(ClientEntity.fromDomain(client));

        CheckingAccount account =
                AccountFactory.checking(client.getId(), clock);

        accountRepositoryAdapter.save(account);

        Transaction transaction =
                Transaction.deposit(
                        account.getId(),
                        account.getAccountIdentity(),
                        Money.of("500"),
                        clock
                );

        adapter.save(transaction);

        assertEquals(1, repository.count());
    }

    @Test
    void shouldFindTransactionsByAccountId() {

        Client client = ClientFactory.create();
        clientRepository.save(ClientEntity.fromDomain(client));

        CheckingAccount account =
                AccountFactory.checking(client.getId(), clock);

        accountRepositoryAdapter.save(account);

        Transaction deposit =
                Transaction.deposit(
                        account.getId(),
                        account.getAccountIdentity(),
                        Money.of("500"),
                        clock
                );

        Transaction withdraw =
                Transaction.withdraw(
                        account.getId(),
                        account.getAccountIdentity(),
                        Money.of("200"),
                        clock
                );

        adapter.save(deposit);
        adapter.save(withdraw);

        List<Transaction> result =
                adapter.findByAccountId(account.getId());

        assertEquals(2, result.size());
    }

    @Test
    void shouldReturnEmptyWhenAccountHasNoTransactions() {

        Client client = ClientFactory.create();
        clientRepository.save(ClientEntity.fromDomain(client));

        CheckingAccount account =
                AccountFactory.checking(client.getId(), clock);

        accountRepositoryAdapter.save(account);

        List<Transaction> result =
                adapter.findByAccountId(account.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTransactionsInCreationOrder() {

        Client client = ClientFactory.create();
        clientRepository.save(ClientEntity.fromDomain(client));

        CheckingAccount account =
                AccountFactory.checking(client.getId(), clock);

        accountRepositoryAdapter.save(account);

        Transaction deposit =
                Transaction.deposit(
                        account.getId(),
                        account.getAccountIdentity(),
                        Money.of("100"),
                        clock
                );

        Transaction withdraw =
                Transaction.withdraw(
                        account.getId(),
                        account.getAccountIdentity(),
                        Money.of("50"),
                        clock
                );

        adapter.save(deposit);
        adapter.save(withdraw);

        List<Transaction> result =
                adapter.findByAccountId(account.getId());

        assertEquals(
                TransactionType.DEPOSIT,
                result.getFirst().getType()
        );

        assertEquals(
                TransactionType.WITHDRAW,
                result.getLast().getType()
        );
    }
}
