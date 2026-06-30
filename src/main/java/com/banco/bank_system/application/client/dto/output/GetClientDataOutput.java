package com.banco.bank_system.application.client.dto.output;

import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;

public record GetClientDataOutput(
        PersonName name,
        CPF cpf,
        Email email
) {
}
