package com.banco.bank_system.presentation.controller;


import com.banco.bank_system.application.client.dto.output.ChangeClientNameOutput;
import com.banco.bank_system.application.client.dto.output.CreateClientOutput;
import com.banco.bank_system.application.client.dto.output.GetClientDataOutput;
import com.banco.bank_system.application.client.usecases.ChangeClientNameUseCase;
import com.banco.bank_system.application.client.usecases.CreateClientUseCase;
import com.banco.bank_system.application.client.usecases.GetClientDataUseCase;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;
import com.banco.bank_system.presentation.dto.request.ChangeClientNameRequest;
import com.banco.bank_system.presentation.dto.request.CreateClientRequest;
import com.banco.bank_system.presentation.dto.response.ChangeNameResponse;
import com.banco.bank_system.presentation.dto.response.ClientData;
import com.banco.bank_system.presentation.dto.response.CreateClientResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final GetClientDataUseCase getClientDataUseCase;
    private final CreateClientUseCase createClientUseCase;
    private final ChangeClientNameUseCase changeClientNameUseCase;

    public ClientController(GetClientDataUseCase getClientDataUseCase,
                            CreateClientUseCase createClientUseCase,
                            ChangeClientNameUseCase changeClientNameUseCase) {
        this.getClientDataUseCase = getClientDataUseCase;
        this.createClientUseCase = createClientUseCase;
        this.changeClientNameUseCase = changeClientNameUseCase;
    }

    @GetMapping(path = "/{cpf}")
    public ResponseEntity<ClientData> clientData(
            @PathVariable String cpf
    ) {

        GetClientDataOutput output = getClientDataUseCase.execute(
                new CPF(cpf)
        );


        return ResponseEntity.ok(
                ClientData.from(output)
        );
    }

    @PostMapping()
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

    @PutMapping()
    public ResponseEntity<ChangeNameResponse> changeClientName(
            @RequestBody ChangeClientNameRequest client
    ) {

        ChangeClientNameOutput output = changeClientNameUseCase.execute(
                new CPF(client.cpf()),
                new PersonName(client.name())
        );

        return ResponseEntity.ok(
                ChangeNameResponse.from(output)
        );
    }

}
