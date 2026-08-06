package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.client.dto.GetClientDataOutput;
import com.banco.bank_system.application.client.util.ClientFinder;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetClientDataUseCase {

    private final ClientFinder clientFinder;

    public GetClientDataUseCase(ClientFinder clientFinder) {
        this.clientFinder = clientFinder;
    }

    @Transactional(readOnly = true)
    public GetClientDataOutput execute(CPF cpf){

        Client client = clientFinder.find(cpf);

        return new GetClientDataOutput(
                client.getName(),
                client.getCpf(),
                client.getEmail()
        );
    }
}
