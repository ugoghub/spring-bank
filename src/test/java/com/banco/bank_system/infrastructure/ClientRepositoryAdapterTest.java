package com.banco.bank_system.infrastructure;

import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.infrastructure.database.adapters.ClientRepositoryAdapter;
import com.banco.bank_system.infrastructure.database.entities.ClientEntity;
import com.banco.bank_system.infrastructure.database.sql.JpaClientRepository;
import com.banco.bank_system.useCase.clientUseCaseTests.helper.ClientFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(ClientRepositoryAdapter.class)
class ClientRepositoryAdapterTest {

    @Autowired
    private ClientRepositoryAdapter adapter;

    @Autowired
    private JpaClientRepository jpaRepository;

    @Test
    void shouldSaveClient() {

        Client client = ClientFactory.create();

        adapter.save(client);

        Optional<ClientEntity> saved =
                jpaRepository.findById(client.getId().id());

        assertTrue(saved.isPresent());

        assertEquals(
                client.getCpf().value(),
                saved.get().getCpf()
        );

        assertEquals(
                client.getEmail().value(),
                saved.get().getEmail()
        );

        assertEquals(
                client.getName().value(),
                saved.get().getName()
        );
    }

    @Test
    void shouldFindClientByCpf() {

        Client client = ClientFactory.create();

        jpaRepository.save(
                ClientEntity.fromDomain(client)
        );

        Optional<Client> result =
                adapter.getClientByCpf(client.getCpf());

        assertTrue(result.isPresent());

        assertEquals(
                client.getId(),
                result.get().getId()
        );

        assertEquals(
                client.getCpf(),
                result.get().getCpf()
        );

        assertEquals(
                client.getEmail(),
                result.get().getEmail()
        );

        assertEquals(
                client.getName(),
                result.get().getName()
        );
    }

    @Test
    void shouldReturnEmptyWhenClientDoesNotExist() {

        Optional<Client> result =
                adapter.getClientByCpf(
                        new CPF("52998224725")
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenCpfExists() {

        Client client = ClientFactory.create();

        jpaRepository.save(
                ClientEntity.fromDomain(client)
        );

        assertTrue(
                adapter.existsByCpf(client.getCpf())
        );
    }

    @Test
    void shouldReturnFalseWhenCpfDoesNotExist() {

        assertFalse(
                adapter.existsByCpf(
                        new CPF("52998224725")
                )
        );
    }

    @Test
    void shouldDeleteClient() {

        Client client = ClientFactory.create();

        jpaRepository.save(
                ClientEntity.fromDomain(client)
        );

        adapter.delete(client.getId());

        assertFalse(
                jpaRepository.existsById(
                        client.getId().id()
                )
        );
    }
}
