package com.banco.bank_system.infrastructure.database.mapper;

import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.ClientId;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;
import com.banco.bank_system.infrastructure.database.entities.ClientEntity;

public final class ClientMapper {

    private ClientMapper() {
    }

    public static ClientEntity fromDomain(Client client) {

        return new ClientEntity(
                client.getId().id(),
                client.getName().value(),
                client.getCpf().value(),
                client.getEmail().value()
        );
    }

    public static Client toDomain(ClientEntity entity) {

        return Client.restore(
                new ClientId(entity.getId()),
                new PersonName(entity.getName()),
                new CPF(entity.getCpf()),
                new Email(entity.getEmail())
        );
    }
}