package com.banco.bank_system.presentation.controller;


import com.banco.bank_system.application.client.dto.output.ChangeClientEmailOutput;
import com.banco.bank_system.application.client.dto.output.ChangeClientNameOutput;
import com.banco.bank_system.application.client.dto.output.CreateClientOutput;
import com.banco.bank_system.application.client.dto.output.GetClientDataOutput;
import com.banco.bank_system.application.client.usecases.*;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;
import com.banco.bank_system.presentation.dto.request.ChangeClientEmailRequest;
import com.banco.bank_system.presentation.dto.request.ChangeClientNameRequest;
import com.banco.bank_system.presentation.dto.request.CreateClientRequest;
import com.banco.bank_system.presentation.dto.response.*;
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

    @PutMapping("/changeName/{cpf}")
    public ResponseEntity<ChangeNameResponse> changeClientName(
            @PathVariable String cpf,
            @RequestBody ChangeClientNameRequest client
    ) {

        ChangeClientNameOutput output = changeClientNameUseCase.execute(
                new CPF(cpf),
                new PersonName(client.name())
        );

        return ResponseEntity.ok(
                ChangeNameResponse.from(output)
        );
    }

    @PutMapping("/changeEmail/{cpf}")
    public ResponseEntity<ChangeEmailResponse> changeClientEmail(
            @PathVariable String cpf,
            @RequestBody ChangeClientEmailRequest client
    ) {

        ChangeClientEmailOutput output = changeClientEmailUseCase.execute(
                new CPF(cpf),
                new Email(client.email())
        );

        return ResponseEntity.ok(
                ChangeEmailResponse.from(output)
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
