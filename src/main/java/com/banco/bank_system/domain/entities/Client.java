package com.banco.bank_system.domain.entities;

import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

public class Client {
    private final UUID id;
    private final CPF cpf;
    private PersonName name;
    private Email email;

    public Client(
            UUID id,
            PersonName name,
            CPF cpf,
            Email email
    ){

        if(id == null){
            throw new IllegalArgumentException("ID inválido");
        }

        if(name == null){
            throw new IllegalArgumentException("Nome inválido");
        }

        if(cpf == null){
            throw new IllegalArgumentException("CPF inválido");
        }

        if(email == null){
            throw new IllegalArgumentException("Email inválido");
        }

        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
    }

    // =========================
    // Actions
    // =========================

    public void changeName(PersonName newName) {

        if(newName == null){
            throw new IllegalArgumentException(
                    "Nome não pode ser null"
            );
        }

        if(this.name.equals(newName)){
            throw new IllegalArgumentException(
                    "Novo nome igual ao atual"
            );
        }


        this.name = newName;
    }

    public void changeEmail(Email newEmail) {

        if(newEmail == null){
            throw new IllegalArgumentException(
                    "Email não pode ser null"
            );
        }


        if(this.email.equals(newEmail)){
            throw new IllegalArgumentException(
                    "Novo email igual ao atual"
            );
        }


        this.email = newEmail;
    }

    public boolean hasSameName(PersonName newName) {
        return this.name.equals(newName);
    }

    public boolean hasSameEmail(Email newEmail) {
        return this.email.equals(newEmail);
    }

    public UUID getId() {
        return id;
    }

    public CPF getCpf() {
        return cpf;
    }

    public PersonName getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
