package com.banco.bank_system.application.client.dto;

import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.ClientId;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;

public record CreateClientOutput(
        ClientId id,
        PersonName name,
        CPF cpf,
        Email email
) {}
