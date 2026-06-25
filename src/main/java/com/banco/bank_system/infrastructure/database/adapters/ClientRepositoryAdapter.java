package com.banco.bank_system.infrastructure.database.adapters;

import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.infrastructure.database.entities.ClientEntity;
import com.banco.bank_system.infrastructure.database.sql.SpringDataClientRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ClientRepositoryAdapter implements ClientRepositoryPort {

    private final SpringDataClientRepository springDataClientRepository;

    public ClientRepositoryAdapter(SpringDataClientRepository springDataClientRepository) {
        this.springDataClientRepository = springDataClientRepository;
    }

    @Override
    public void delete(UUID uuid) {

    }

    @Override
    public void put(Email email, Client client) {

    }

    @Override
    public Client getClientByCpf(CPF cpf) {
        return null;
    }

    @Override
    public boolean existsByEmail(Email newEmail) {
        return false;
    }

    @Override
    public boolean existsByCpf(CPF cpf) {
        return false;
    }

    @Override
    public boolean existsById(UUID clientId) {
        return false;
    }

    @Override
    public void save(Client client) {
        // Traduz o Client (Domínio) para ClientEntity (Infra)
        ClientEntity entity = ClientEntity.fromDomain(client);

        // Salva a entidade de infraestrutura no banco
        springDataClientRepository.save(entity);
    }
}
