package com.banco.bank_system.infrastructure;

import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.ClientId;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.infrastructure.database.adapters.ClientRepositoryAdapter;
import com.banco.bank_system.infrastructure.database.entities.ClientEntity;
import com.banco.bank_system.infrastructure.database.mapper.ClientMapper;
import com.banco.bank_system.infrastructure.database.sql.JpaClientRepository;
import com.banco.bank_system.useCase.client.helper.ClientFactory;
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
                ClientMapper.fromDomain(client)
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
    void shouldReturnTrueWhenEmailExists() {

        Client client = ClientFactory.create();

        jpaRepository.save(ClientMapper.fromDomain(client));

        assertTrue(
                adapter.existsByEmail(client.getEmail())
        );
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {

        assertFalse(
                adapter.existsByEmail(
                        new Email("teste@email.com")
                )
        );
    }

    @Test
    void shouldReturnTrueWhenCpfExists() {

        Client client = ClientFactory.create();

        jpaRepository.save(
                ClientMapper.fromDomain(client)
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
                ClientMapper.fromDomain(client)
        );

        adapter.delete(client.getId());

        assertFalse(
                jpaRepository.existsById(
                        client.getId().id()
                )
        );
    }

    @Test
    void shouldFindClientById() {

        Client client = ClientFactory.create();

        jpaRepository.save(ClientMapper.fromDomain(client));

        Optional<Client> result =
                adapter.findById(client.getId());

        assertTrue(result.isPresent());

        assertEquals(
                client.getId(),
                result.get().getId()
        );
    }

    @Test
    void shouldReturnEmptyWhenIdDoesNotExist() {

        Optional<Client> result =
                adapter.findById(
                        ClientId.generate()
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnTrueWhenIdExists() {

        Client client = ClientFactory.create();

        jpaRepository.save(ClientMapper.fromDomain(client));

        assertTrue(
                adapter.existsById(client.getId())
        );
    }

    @Test
    void shouldReturnFalseWhenIdDoesNotExist() {

        assertFalse(
                adapter.existsById(
                        ClientId.generate()
                )
        );
    }
}
