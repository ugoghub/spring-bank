package com.banco.bank_system.presentation.controller;


import com.banco.bank_system.application.client.dto.CreateClientOutput;
import com.banco.bank_system.application.client.dto.GetClientDataOutput;
import com.banco.bank_system.application.client.usecases.*;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;
import com.banco.bank_system.presentation.dto.request.client.ChangeClientEmailRequest;
import com.banco.bank_system.presentation.dto.request.client.ChangeClientNameRequest;
import com.banco.bank_system.presentation.dto.request.client.CreateClientRequest;
import com.banco.bank_system.presentation.dto.response.client.ClientDataResponse;
import com.banco.bank_system.presentation.dto.response.client.CreateClientResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final GetClientDataUseCase getClientDataUseCase;
    private final CreateClientUseCase createClientUseCase;
    private final ChangeClientNameUseCase changeClientNameUseCase;
    private final ChangeClientEmailUseCase changeClientEmailUseCase;
    private final RemoveClientUseCase removeClientUseCase;

    public ClientController(GetClientDataUseCase getClientDataUseCase,
                            CreateClientUseCase createClientUseCase,
                            ChangeClientNameUseCase changeClientNameUseCase,
                            ChangeClientEmailUseCase changeClientEmailUseCase,
                            RemoveClientUseCase removeClientUseCase) {
        this.getClientDataUseCase = getClientDataUseCase;
        this.createClientUseCase = createClientUseCase;
        this.changeClientNameUseCase = changeClientNameUseCase;
        this.changeClientEmailUseCase = changeClientEmailUseCase;
        this.removeClientUseCase = removeClientUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateClientResponse> createClient(
            @RequestBody CreateClientRequest client
    ) {

        CreateClientOutput output = createClientUseCase.execute(
                new PersonName(client.name()),
                new CPF(client.cpf()),
                new Email(client.email())
        );

        return ResponseEntity.ok(
                CreateClientResponse.from(output)
        );
    }

    @GetMapping(path = "/{cpf}")
    public ResponseEntity<ClientDataResponse> clientData(
            @PathVariable String cpf
    ) {

        GetClientDataOutput output = getClientDataUseCase.execute(
                new CPF(cpf)
        );


        return ResponseEntity.ok(
                ClientDataResponse.from(output)
        );
    }

    @PutMapping("/changeName/{cpf}")
    public ResponseEntity<ClientDataResponse> changeClientName(
            @PathVariable String cpf,
            @RequestBody ChangeClientNameRequest client
    ) {

        GetClientDataOutput output = changeClientNameUseCase.execute(
                new CPF(cpf),
                new PersonName(client.name())
        );

        return ResponseEntity.ok(
                ClientDataResponse.from(output)
        );
    }

    @PutMapping("/changeEmail/{cpf}")
    public ResponseEntity<ClientDataResponse> changeClientEmail(
            @PathVariable String cpf,
            @RequestBody ChangeClientEmailRequest client
    ) {

        GetClientDataOutput output = changeClientEmailUseCase.execute(
                new CPF(cpf),
                new Email(client.email())
        );

        return ResponseEntity.ok(
                ClientDataResponse.from(output)
        );
    }

    @DeleteMapping(path = "/{cpf}")
    public ResponseEntity<Void> removeClient(
        @PathVariable String cpf
    ){
        removeClientUseCase.execute(new CPF(cpf));

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
