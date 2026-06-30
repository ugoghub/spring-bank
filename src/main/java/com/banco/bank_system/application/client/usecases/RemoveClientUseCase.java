package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.application.exception.ClientNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RemoveClientUseCase {

    private final ClientRepositoryPort clientRepository;
    private final RemoveAllAccountsUseCase removeAllAccountsUseCase;

    public RemoveClientUseCase(ClientRepositoryPort clientRepository,
                               RemoveAllAccountsUseCase removeAllAccountsUseCase) {
        this.clientRepository = clientRepository;
        this.removeAllAccountsUseCase = removeAllAccountsUseCase;
    }

    public void execute(CPF cpf){
        Client client =
                clientRepository.getClientByCpf(cpf)
                        .orElseThrow(ClientNotFoundException::new);

        removeAllAccountsUseCase.execute(client.getId());

        clientRepository.delete(client.getId());
    }
}
