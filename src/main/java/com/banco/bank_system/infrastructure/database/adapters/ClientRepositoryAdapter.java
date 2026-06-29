package com.banco.bank_system.infrastructure.database.adapters;

import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.infrastructure.database.entities.ClientEntity;
import com.banco.bank_system.infrastructure.database.sql.SpringDataClientRepository;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ClientRepositoryAdapter
        implements ClientRepositoryPort {

    private final SpringDataClientRepository repository;

    public ClientRepositoryAdapter(
            SpringDataClientRepository repository
    ) {

        this.repository = repository;

    }


    @Override
    public void delete(UUID id) {

        repository.deleteById(id);

    }


    @Override
    public Optional<Client> getClientByCpf(CPF cpf) {

        return repository
                .findByCpf(cpf.value())
                .map(ClientEntity::toDomain);

    }


    @Override
    public boolean existsByEmail(
            Email email
    ) {


        return repository.existsByEmail(
                email.value()
        );

    }


    @Override
    public boolean existsByCpf(
            CPF cpf
    ) {


        return repository.existsByCpf(
                cpf.value()
        );

    }


    @Override
    public boolean existsById(
            UUID clientId
    ) {


        return repository.existsById(clientId);

    }


    @Override
    public Optional<Client> findById(UUID clientId) {
        return repository.findById(clientId)
                .map(ClientEntity::toDomain);
    }

    @Override
    public void save(Client client) {


        ClientEntity entity =
                ClientEntity.fromDomain(client);


         repository.save(entity);

    }

}