package com.banco.bank_system.infrastructure.database.entities;

import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "tb_clients")
@Getter
@Setter
@NoArgsConstructor
public class ClientEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String cpf;

    public ClientEntity(String name, String cpf, String email) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
    }

    public Client toDomain() {
        return new Client(new PersonName(name), new CPF(cpf), new Email(this.email));
    }

    public static ClientEntity fromDomain(Client client) {
        return new ClientEntity(
                client.getName().value(),
                client.getCpf().value(),
                client.getEmail().value()
        );
    }
}