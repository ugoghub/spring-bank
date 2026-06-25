package com.banco.bank_system.application.client.port;

import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.infrastructure.database.entities.ClientEntity;

import java.util.UUID;

public interface ClientRepositoryPort {
    void save(Client client);

    void delete(UUID uuid);

    void put(Email email, Client client);

    Client getClientByCpf(CPF cpf);

    boolean existsByEmail(Email newEmail);

    boolean existsByCpf(CPF cpf);

    boolean existsById(UUID clientId);
}
