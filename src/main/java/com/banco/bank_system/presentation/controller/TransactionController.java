package com.banco.bank_system.presentation.controller;


import com.banco.bank_system.application.transaction.dto.DepositOutput;
import com.banco.bank_system.application.transaction.dto.TransferOutput;
import com.banco.bank_system.application.transaction.dto.WithdrawOutput;
import com.banco.bank_system.application.transaction.usecases.DepositUseCase;
import com.banco.bank_system.application.transaction.usecases.TransferUseCase;
import com.banco.bank_system.application.transaction.usecases.WithdrawUseCase;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.presentation.dto.request.DepositRequest;
import com.banco.bank_system.presentation.dto.request.TransferRequest;
import com.banco.bank_system.presentation.dto.request.WithdrawRequest;
import com.banco.bank_system.presentation.dto.response.DepositResponse;
import com.banco.bank_system.presentation.dto.response.TransferResponse;
import com.banco.bank_system.presentation.dto.response.WithdrawResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final TransferUseCase transferUseCase;

    private final Clock clock;

    public TransactionController(
            DepositUseCase depositUseCase,
            WithdrawUseCase withdrawUseCase,
            TransferUseCase transferUseCase,
            Clock clock
    ) {
        this.depositUseCase = depositUseCase;
        this.withdrawUseCase = withdrawUseCase;
        this.transferUseCase = transferUseCase;

        this.clock = Clock.systemUTC();
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
                Money.of(request.amount()),
                clock
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
                Money.of(request.amount()),
                clock
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
                Money.of(request.amount()),
                clock
        );

        return ResponseEntity.ok(
                TransferResponse.from(output)
        );
    }
}
