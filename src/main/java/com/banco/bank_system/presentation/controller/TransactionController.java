package com.banco.bank_system.presentation.controller;


import com.banco.bank_system.application.transaction.dto.DepositOutput;
import com.banco.bank_system.application.transaction.dto.TransactionDTO;
import com.banco.bank_system.application.transaction.dto.TransferOutput;
import com.banco.bank_system.application.transaction.dto.WithdrawOutput;
import com.banco.bank_system.application.transaction.usecases.DepositUseCase;
import com.banco.bank_system.application.transaction.usecases.GetTransactionsUseCase;
import com.banco.bank_system.application.transaction.usecases.TransferUseCase;
import com.banco.bank_system.application.transaction.usecases.WithdrawUseCase;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.presentation.dto.request.transactions.DepositRequest;
import com.banco.bank_system.presentation.dto.request.transactions.TransferRequest;
import com.banco.bank_system.presentation.dto.request.transactions.WithdrawRequest;
import com.banco.bank_system.presentation.dto.response.transactions.DepositResponse;
import com.banco.bank_system.presentation.dto.response.transactions.TransactionResponse;
import com.banco.bank_system.presentation.dto.response.transactions.TransferResponse;
import com.banco.bank_system.presentation.dto.response.transactions.WithdrawResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final TransferUseCase transferUseCase;
    private final GetTransactionsUseCase getAccountTransactionsUseCase;

    public TransactionController(
            DepositUseCase depositUseCase,
            WithdrawUseCase withdrawUseCase,
            TransferUseCase transferUseCase,
            GetTransactionsUseCase getAccountTransactionsUseCase
    ) {
        this.depositUseCase = depositUseCase;
        this.withdrawUseCase = withdrawUseCase;
        this.getAccountTransactionsUseCase = getAccountTransactionsUseCase;
        this.transferUseCase = transferUseCase;
    }

    @PostMapping("/deposit")
    public ResponseEntity<DepositResponse> deposit(
            @RequestBody DepositRequest request
    ) {

        DepositOutput output = depositUseCase.execute(
                new AccountIdentity(
                        request.branch(),
                        request.accountNumber()
                ),
                Money.of(request.amount())
        );

        return ResponseEntity.ok(
                DepositResponse.from(output)
        );
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawResponse> withdraw(
            @RequestBody WithdrawRequest request
    ) {

        WithdrawOutput output = withdrawUseCase.execute(
                new AccountIdentity(
                        request.branch(),
                        request.accountNumber()
                ),
                Money.of(request.amount())
        );

        return ResponseEntity.ok(
                WithdrawResponse.from(output)
        );
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(
            @RequestBody TransferRequest request
    ) {

        TransferOutput output = transferUseCase.execute(
                new AccountIdentity(
                        request.fromBranch(),
                        request.fromAccountNumber()
                ),
                new AccountIdentity(
                        request.toBranch(),
                        request.toAccountNumber()
                ),
                Money.of(request.amount())
        );

        return ResponseEntity.ok(
                TransferResponse.from(output)
        );
    }

    @GetMapping("/{branch}/{accountNumber}")
    public ResponseEntity<List<TransactionResponse>> getTransactions(
            @PathVariable String branch,
            @PathVariable String accountNumber
    ){
        AccountIdentity accountIdentity = new AccountIdentity(branch, accountNumber);

        List<TransactionDTO> output = getAccountTransactionsUseCase.execute(accountIdentity);

        return ResponseEntity.ok(
                TransactionResponse.from(output)
        );
    }
}
