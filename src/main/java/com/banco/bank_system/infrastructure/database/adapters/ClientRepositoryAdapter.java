package com.banco.bank_system.infrastructure.database.adapters;

import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.ClientId;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.infrastructure.database.entities.ClientEntity;
import com.banco.bank_system.infrastructure.database.sql.JpaClientRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClientRepositoryAdapter
        implements ClientRepositoryPort {

    private final JpaClientRepository repository;

    public ClientRepositoryAdapter(
            JpaClientRepository repository
    ) {

        this.repository = repository;
    }


    @Override
    public void delete(ClientId clientId) {

        repository.deleteById(clientId.id());
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
            ClientId clientId
    ) {

        return repository.existsById(clientId.id());
    }


    @Override
    public Optional<Client> findById(ClientId clientId) {
        return repository.findById(clientId.id())
                .map(ClientEntity::toDomain);
    }

    @Override
    public void save(Client client) {

        repository.save(ClientEntity.fromDomain(client));
    }

}