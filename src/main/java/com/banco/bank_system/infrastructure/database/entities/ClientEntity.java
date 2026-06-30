package com.banco.bank_system.infrastructure.database.entities;


import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.UUID;


@Entity
@Table(name = "tb_clients")
@Getter
@NoArgsConstructor
public class ClientEntity {


    @Id
    @Column(
            nullable = false,
            updatable = false
    )
    private UUID id;


    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private String name;


    @Column(
            name = "CPF",
            nullable = false,
            updatable = false
    )
    private String cpf;



    public ClientEntity(
            UUID id,
            String name,
            String cpf,
            String email
    ){

        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;

    }



    public Client toDomain(){

        return new Client(

                id,

                new PersonName(name),

                new CPF(cpf),

                new Email(email)

        );

    }



    public static ClientEntity fromDomain(Client client){


        return new ClientEntity(

                client.getId(),

                client.getName().value(),

                client.getCpf().value(),

                client.getEmail().value()

        );

    }

}