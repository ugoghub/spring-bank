package com.banco.bank_system.infrastructure;

import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.entities.SavingsAccount;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.enums.TransactionType;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.domain.valueobject.OperationId;
import com.banco.bank_system.entities.helper.AccountFactory;
import com.banco.bank_system.infrastructure.database.adapters.AccountRepositoryAdapter;
import com.banco.bank_system.infrastructure.database.adapters.TransactionRepositoryAdapter;
import com.banco.bank_system.infrastructure.database.entities.ClientEntity;
import com.banco.bank_system.infrastructure.database.entities.TransactionEntity;
import com.banco.bank_system.infrastructure.database.sql.JpaClientRepository;
import com.banco.bank_system.infrastructure.database.sql.JpaTransactionRepository;
import com.banco.bank_system.useCase.clientUseCaseTests.helper.ClientFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

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

        TransactionEntity entity = repository.findAll().getFirst();

        assertEquals(
                transaction.getType(),
                entity.getType()
        );

        assertEquals(
                transaction.getAmount().value(),
                entity.getAmount()
        );

        assertEquals(
                transaction.getAccountId().id(),
                entity.getAccountId()
        );
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

        Page<Transaction> result =
                adapter.findByAccountId(
                        account.getId(),
                        PageRequest.of(0, 10)
                );

        assertEquals(2, result.getTotalElements());

        assertEquals(
                TransactionType.DEPOSIT,
                result.getContent().getFirst().getType()
        );

        assertEquals(
                TransactionType.WITHDRAW,
                result.getContent().getLast().getType()
        );
    }

    @Test
    void shouldReturnEmptyWhenAccountHasNoTransactions() {

        Client client = ClientFactory.create();
        clientRepository.save(ClientEntity.fromDomain(client));

        CheckingAccount account =
                AccountFactory.checking(client.getId(), clock);

        accountRepositoryAdapter.save(account);

        Page<Transaction> result =
                adapter.findByAccountId(
                        account.getId(),
                        PageRequest.of(0, 10)
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldSaveTransferSentTransaction() {

        Client client = ClientFactory.create();
        clientRepository.save(ClientEntity.fromDomain(client));

        CheckingAccount from =
                AccountFactory.checking(client.getId(), clock);

        SavingsAccount to =
                AccountFactory.savings(client.getId(), clock);

        accountRepositoryAdapter.save(from);
        accountRepositoryAdapter.save(to);

        OperationId operationId = OperationId.generate();

        Transaction transaction =
                Transaction.transferSent(
                        from.getId(),
                        operationId,
                        from.getAccountIdentity(),
                        to.getAccountIdentity(),
                        Money.of("200"),
                        clock
                );

        adapter.save(transaction);

        TransactionEntity entity =
                repository.findAll().getFirst();

        assertEquals(
                TransactionType.TRANSFER_SENT,
                entity.getType()
        );

        assertEquals(
                operationId.id(),
                entity.getOperationId()
        );

        assertEquals(
                from.getId().id(),
                entity.getAccountId()
        );

        assertEquals(
                from.getAccountIdentity().branch(),
                entity.getSource_branch()
        );

        assertEquals(
                from.getAccountIdentity().accountNumber(),
                entity.getSource_accountNumber()
        );

        assertEquals(
                to.getAccountIdentity().branch(),
                entity.getDestination_branch()
        );

        assertEquals(
                to.getAccountIdentity().accountNumber(),
                entity.getDestination_accountNumber()
        );

        assertEquals(
                Money.of("200").value(),
                entity.getAmount()
        );
    }

    @Test
    void shouldSaveTransferReceivedTransaction() {

        Client client = ClientFactory.create();
        clientRepository.save(ClientEntity.fromDomain(client));

        CheckingAccount from =
                AccountFactory.checking(client.getId(), clock);

        SavingsAccount to =
                AccountFactory.savings(client.getId(), clock);

        accountRepositoryAdapter.save(from);
        accountRepositoryAdapter.save(to);

        OperationId operationId = OperationId.generate();

        Transaction transaction =
                Transaction.transferReceived(
                        to.getId(),
                        operationId,
                        from.getAccountIdentity(),
                        to.getAccountIdentity(),
                        Money.of("200"),
                        clock
                );

        adapter.save(transaction);

        TransactionEntity entity =
                repository.findAll().getFirst();

        assertEquals(
                TransactionType.TRANSFER_RECEIVED,
                entity.getType()
        );

        assertEquals(
                operationId.id(),
                entity.getOperationId()
        );

        assertEquals(
                to.getId().id(),
                entity.getAccountId()
        );

        assertEquals(
                from.getAccountIdentity().branch(),
                entity.getSource_branch()
        );

        assertEquals(
                from.getAccountIdentity().accountNumber(),
                entity.getSource_accountNumber()
        );

        assertEquals(
                to.getAccountIdentity().branch(),
                entity.getDestination_branch()
        );

        assertEquals(
                to.getAccountIdentity().accountNumber(),
                entity.getDestination_accountNumber()
        );

        assertEquals(
                Money.of("200").value(),
                entity.getAmount()
        );
    }

    @Test
    void shouldRestoreTransferSentTransaction() {

        Client client = ClientFactory.create();
        clientRepository.save(ClientEntity.fromDomain(client));

        CheckingAccount from =
                AccountFactory.checking(client.getId(), clock);

        SavingsAccount to =
                AccountFactory.savings(client.getId(), clock);

        accountRepositoryAdapter.save(from);
        accountRepositoryAdapter.save(to);

        OperationId operationId = OperationId.generate();

        Transaction sent =
                Transaction.transferSent(
                        from.getId(),
                        operationId,
                        from.getAccountIdentity(),
                        to.getAccountIdentity(),
                        Money.of("200"),
                        clock
                );

        adapter.save(sent);

        Page<Transaction> result =
                adapter.findByAccountId(
                        from.getId(),
                        PageRequest.of(0, 10)
                );

        assertEquals(1, result.getTotalElements());

        Transaction restored = result.getContent().getFirst();

        assertEquals(
                TransactionType.TRANSFER_SENT,
                restored.getType()
        );

        assertEquals(
                operationId,
                restored.getOperationId()
        );

        assertEquals(
                from.getId(),
                restored.getAccountId()
        );

        assertEquals(
                from.getAccountIdentity(),
                restored.getSource()
        );

        assertEquals(
                to.getAccountIdentity(),
                restored.getDestination()
        );

        assertEquals(
                Money.of("200"),
                restored.getAmount()
        );

        assertEquals(
                LocalDateTime.of(2026, 1, 10, 10, 0),
                restored.getDateTime()
        );
    }

    @Test
    void shouldRestoreTransferReceivedTransaction() {

        Client client = ClientFactory.create();
        clientRepository.save(ClientEntity.fromDomain(client));

        CheckingAccount from =
                AccountFactory.checking(client.getId(), clock);

        SavingsAccount to =
                AccountFactory.savings(client.getId(), clock);

        accountRepositoryAdapter.save(from);
        accountRepositoryAdapter.save(to);

        OperationId operationId = OperationId.generate();

        Transaction received =
                Transaction.transferReceived(
                        to.getId(),
                        operationId,
                        from.getAccountIdentity(),
                        to.getAccountIdentity(),
                        Money.of("200"),
                        clock
                );

        adapter.save(received);

        Page<Transaction> result =
                adapter.findByAccountId(
                        to.getId(),
                        PageRequest.of(0, 10)
                );

        assertEquals(1, result.getTotalElements());

        Transaction restored = result.getContent().getFirst();

        assertEquals(
                TransactionType.TRANSFER_RECEIVED,
                restored.getType()
        );

        assertEquals(
                operationId,
                restored.getOperationId()
        );

        assertEquals(
                to.getId(),
                restored.getAccountId()
        );

        assertEquals(
                from.getAccountIdentity(),
                restored.getSource()
        );

        assertEquals(
                to.getAccountIdentity(),
                restored.getDestination()
        );

        assertEquals(
                Money.of("200"),
                restored.getAmount()
        );

        assertEquals(
                LocalDateTime.of(2026, 1, 10, 10, 0),
                restored.getDateTime()
        );
    }
}
