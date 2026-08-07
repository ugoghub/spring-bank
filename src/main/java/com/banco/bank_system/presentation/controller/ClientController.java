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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@Tag(name = "Clientes", description = "Operações relacionadas aos clientes")
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

    @Operation(
            summary = "Cadastrar cliente",
            description = "Realiza o cadastro de um novo cliente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado"),
            @ApiResponse(responseCode = "409", description = "CPF ou e-mail já cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<CreateClientResponse> createClient(
            @Valid @RequestBody CreateClientRequest request
    ) {

        CreateClientOutput output = createClientUseCase.execute(
                new PersonName(request.name()),
                new CPF(request.cpf()),
                new Email(request.email())
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CreateClientResponse.from(output));
    }


    @Operation(
            summary = "Consultar cliente",
            description = "Consulta os dados de um cliente a partir do CPF."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "400", description = "CPF inválido")
    })
    @GetMapping(path = "/{cpf}")
    public ResponseEntity<ClientDataResponse> getClientData(
            @Parameter(
                    description = "CPF do cliente",
                    example = "52998224725"
            )
            @PathVariable String cpf
    ) {

        GetClientDataOutput output = getClientDataUseCase.execute(
                new CPF(cpf)
        );


        return ResponseEntity.ok(
                ClientDataResponse.from(output)
        );
    }

    @Operation(
            summary = "Atualizar o nome do cliente",
            description = "Altera o nome de um cliente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Nome alterado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PatchMapping("/{cpf}/name")
    public ResponseEntity<ClientDataResponse> changeClientName(

            @Parameter(
                    description = "CPF do cliente",
                    example = "52998224725"
            )
            @PathVariable String cpf,

            @Valid @RequestBody ChangeClientNameRequest request
    ) {

        GetClientDataOutput output = changeClientNameUseCase.execute(
                new CPF(cpf),
                new PersonName(request.name())
        );

        return ResponseEntity.ok(
                ClientDataResponse.from(output)
        );
    }

    @Operation(
            summary = "Atualizar o email do cliente",
            description = "Altera o e-mail de um cliente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "E-mail alterado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PatchMapping("/{cpf}/email")
    public ResponseEntity<ClientDataResponse> changeClientEmail(

            @Parameter(
                    description = "CPF do cliente",
                    example = "52998224725"
            )
            @PathVariable String cpf,

            @Valid @RequestBody ChangeClientEmailRequest request
    ) {

        GetClientDataOutput output = changeClientEmailUseCase.execute(
                new CPF(cpf),
                new Email(request.email())
        );

        return ResponseEntity.ok(
                ClientDataResponse.from(output)
        );
    }

    @Operation(
            summary = "Remover cliente",
            description = "Remove um cliente pelo CPF."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "400", description = "CPF inválido")
    })
    @DeleteMapping(path = "/{cpf}")
    public ResponseEntity<Void> removeClient(
            @Parameter(
                    description = "CPF do cliente",
                    example = "52998224725"
            )
            @PathVariable String cpf
    ) {
        removeClientUseCase.execute(new CPF(cpf));

        return ResponseEntity.noContent().build();
    }
}
