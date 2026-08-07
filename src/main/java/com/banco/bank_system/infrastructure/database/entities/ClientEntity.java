package com.banco.bank_system.infrastructure.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "tb_clients")

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
            name="cpf",
            nullable=false,
            unique=true,
            updatable=false
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
}