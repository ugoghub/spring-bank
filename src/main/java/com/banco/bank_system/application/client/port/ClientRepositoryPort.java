package com.banco.bank_system.application.client.port;

import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.ClientId;
import com.banco.bank_system.domain.valueobject.Email;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepositoryPort {
    void save(Client client);

    void delete(ClientId uuid);

    Optional<Client> getClientByCpf(CPF cpf);

    Optional<Client> findById(ClientId clientId);

    boolean existsByEmail(Email newEmail);

    boolean existsByCpf(CPF cpf);

    boolean existsById(ClientId clientId);
}
