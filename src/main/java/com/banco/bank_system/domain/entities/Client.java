package com.banco.bank_system.domain.entities;

import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Client {
    private final UUID id;
    private final CPF cpf;
    private PersonName name;
    private Email email;

    public Client(PersonName name, CPF cpf, Email email) {
        this.id = UUID.randomUUID();
        this.cpf = cpf;
        this.name = name;
        this.email = email;
    }

    // =========================
    // Actions
    // =========================

    public void changeName(PersonName newName) {
        this.name = newName;
    }

    public void changeEmail(Email newEmail) {
        this.email = newEmail;
    }

    public boolean hasSameName(PersonName newName) {
        return this.name.equals(newName);
    }

    public boolean hasSameEmail(Email newEmail) {
        return this.email.equals(newEmail);
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
