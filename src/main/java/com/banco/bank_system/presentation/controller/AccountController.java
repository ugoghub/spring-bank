package com.banco.bank_system.presentation.controller;

import com.banco.bank_system.application.account.dto.CreateAccountOutput;
import com.banco.bank_system.application.account.dto.GetBalanceOutput;
import com.banco.bank_system.application.account.dto.GetClientAccountOutput;
import com.banco.bank_system.application.account.dto.GetClientAccountsOutput;
import com.banco.bank_system.application.account.usecases.*;
import com.banco.bank_system.domain.enums.AccountType;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.presentation.dto.request.account.CreateAccountRequest;
import com.banco.bank_system.presentation.dto.response.account.BalanceResponse;
import com.banco.bank_system.presentation.dto.response.account.CreateAccountResponse;
import com.banco.bank_system.presentation.dto.response.account.GetClientAccountResponse;
import com.banco.bank_system.presentation.dto.response.account.GetClientAccountsResponse;
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
@RequestMapping("/accounts")
@Tag(name = "Contas", description = "Operações relacionadas às contas dos clientes")
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountBalanceUseCase getAccountBalanceUseCase;
    private final GetClientAccountsUseCase getClientAccountsUseCase;
    private final GetAccountUseCase getClientAccountUseCase;
    private final RemoveAccountUseCase removeAccountUseCase;

    public AccountController(CreateAccountUseCase createAccountUseCase,
                             GetAccountBalanceUseCase getAccountBalanceUseCase,
                             GetClientAccountsUseCase getClientAccountsUseCase,
                             GetAccountUseCase getClientAccountUseCase,
                             RemoveAccountUseCase removeAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountBalanceUseCase = getAccountBalanceUseCase;
        this.getClientAccountUseCase = getClientAccountUseCase;
        this.getClientAccountsUseCase = getClientAccountsUseCase;
        this.removeAccountUseCase = removeAccountUseCase;
    }

    @Operation(
            summary = "Criar conta",
            description = "Cria uma conta corrente ou poupança para um cliente existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PostMapping
    public ResponseEntity<CreateAccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request
    ){

        CPF cpf = new CPF(request.cpf());
        AccountType accountType = AccountType.from(request.accountType());

        CreateAccountOutput output = createAccountUseCase.execute(cpf, accountType);

        CreateAccountResponse response = CreateAccountResponse.from(output);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }



    @Operation(
            summary = "Buscar saldo",
            description = "Retorna o saldo de uma conta."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Saldo retornado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/balance/{branch}/{accountNumber}")
    public ResponseEntity<BalanceResponse> getBalance(
            @Parameter(
                    description = "Agência da conta",
                    example = "01"
            )
            @PathVariable String branch,

            @Parameter(
                    description = "Número da conta",
                    example = "123456-1"
            )
            @PathVariable String accountNumber
    ){

        AccountIdentity accountIdentity = new AccountIdentity(branch, accountNumber);

        GetBalanceOutput output = getAccountBalanceUseCase.execute(accountIdentity);

        return ResponseEntity.ok(
                BalanceResponse.from(output)
        );
    }


    @Operation(
            summary = "Consultar contas do cliente",
            description = "Retorna todas as contas pertencentes a um cliente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contas retornadas com sucesso"),
            @ApiResponse(responseCode = "400", description = "CPF inválido"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{cpf}")
    public ResponseEntity<GetClientAccountsResponse> getAccounts(

            @Parameter(
                    description = "CPF do cliente",
                    example = "52998224725"
            )
            @PathVariable String cpf
    ){

        GetClientAccountsOutput output = getClientAccountsUseCase.execute(new CPF(cpf));

        return ResponseEntity.ok(
                GetClientAccountsResponse.from(output)
        );
    }

    @Operation(
            summary = "Consultar conta",
            description = "Retorna os dados de uma conta."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/{branch}/{accountNumber}")
    public ResponseEntity<GetClientAccountResponse> getAccount(
            @Parameter(
                    description = "Agência da conta",
                    example = "01"
            )
            @PathVariable String branch,

            @Parameter(
                    description = "Número da conta",
                    example = "123456-1"
            )
            @PathVariable String accountNumber
    ){
        AccountIdentity accountIdentity = new AccountIdentity(branch, accountNumber);

        GetClientAccountOutput output = getClientAccountUseCase.execute(accountIdentity);

        return ResponseEntity.ok(
                GetClientAccountResponse.from(output)
        );
    }

    @Operation(
            summary = "Remover conta",
            description = "Remove uma conta sem saldo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Conta removida com sucesso"),
            @ApiResponse(responseCode = "400", description = "A conta possui saldo ou os dados são inválidos"),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @DeleteMapping("/delete/{branch}/{accountNumber}")
    public ResponseEntity<Void> delete(
            @Parameter(
                    description = "Agência da conta",
                    example = "01"
            )
            @PathVariable String branch,

            @Parameter(
                    description = "Número da conta",
                    example = "123456-1"
            )
            @PathVariable String accountNumber
    ){
        AccountIdentity accountIdentity = new AccountIdentity(branch, accountNumber);

        removeAccountUseCase.execute(accountIdentity);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
