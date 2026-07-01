package com.banco.bank_system.application.transaction.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.exception.AccountNotFoundException;
import com.banco.bank_system.application.transaction.dto.GetTransactionsOutput;
import com.banco.bank_system.application.transaction.port.TransactionRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetTransactionsUseCase {

    private final TransactionRepositoryPort transactionRepository;
    private final AccountRepositoryPort accountRepository;

    public GetTransactionsUseCase(TransactionRepositoryPort transactionRepository, AccountRepositoryPort accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository =accountRepository;
    }

    public GetTransactionsOutput execute(AccountIdentity accountIdentity){

        Account account = accountRepository.getAccountByAccountIdentity(accountIdentity)
                .orElseThrow(AccountNotFoundException::new);

        List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());

        return new GetTransactionsOutput(
                GetTransactionsOutput.from(transactions)
        );
    }
}
