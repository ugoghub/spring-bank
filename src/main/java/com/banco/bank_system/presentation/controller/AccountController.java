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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final CreateAccountUseCase createAccountUseCase;
    private final GetAccountBalanceUseCase getAccountBalanceUseCase;
    private final GetClientAccountsUseCase getClientAccountsUseCase;
    private final GetClientAccountUseCase getClientAccountUseCase;
    private final RemoveAccountUseCase removeAccountUseCase;

    public AccountController(CreateAccountUseCase createAccountUseCase,
                             GetAccountBalanceUseCase getAccountBalanceUseCase,
                             GetClientAccountsUseCase getClientAccountsUseCase,
                             GetClientAccountUseCase getClientAccountUseCase,
                             RemoveAccountUseCase removeAccountUseCase) {
        this.createAccountUseCase = createAccountUseCase;
        this.getAccountBalanceUseCase = getAccountBalanceUseCase;
        this.getClientAccountUseCase = getClientAccountUseCase;
        this.getClientAccountsUseCase = getClientAccountsUseCase;
        this.removeAccountUseCase = removeAccountUseCase;
    }

    @PostMapping
    public ResponseEntity<CreateAccountResponse> createAccount(
            @RequestBody CreateAccountRequest request
    ){

        CPF cpf = new CPF(request.cpf());
        AccountType accountType = AccountType.from(request.accountType());

        CreateAccountOutput output = createAccountUseCase.execute(cpf, accountType);

        CreateAccountResponse response = CreateAccountResponse.from(output);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/balance/{branch}/{accountNumber}")
    public ResponseEntity<BalanceResponse> getBalance(
            @PathVariable String branch,
            @PathVariable String accountNumber
    ){

        AccountIdentity accountIdentity = new AccountIdentity(branch, accountNumber);

        GetBalanceOutput output = getAccountBalanceUseCase.execute(accountIdentity);

        return ResponseEntity.ok(
                BalanceResponse.from(output)
        );
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<GetClientAccountsResponse> getAccounts(
            @PathVariable String cpf
    ){

        GetClientAccountsOutput output = getClientAccountsUseCase.execute(new CPF(cpf));

        return ResponseEntity.ok(
                GetClientAccountsResponse.from(output)
        );
    }

    @GetMapping("/{branch}/{accountNumber}")
    public ResponseEntity<GetClientAccountResponse> getAccount(
            @PathVariable String branch,
            @PathVariable String accountNumber
    ){
        AccountIdentity accountIdentity = new AccountIdentity(branch, accountNumber);

        GetClientAccountOutput output = getClientAccountUseCase.execute(accountIdentity);

        return ResponseEntity.ok(
                GetClientAccountResponse.from(output)
        );
    }

    @DeleteMapping("/delete/{branch}/{accountNumber}")
    public ResponseEntity<Void> delete(
            @PathVariable String branch,
            @PathVariable String accountNumber
    ){
        AccountIdentity accountIdentity = new AccountIdentity(branch, accountNumber);

        removeAccountUseCase.execute(accountIdentity);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
