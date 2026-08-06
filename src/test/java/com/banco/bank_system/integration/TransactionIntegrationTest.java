package com.banco.bank_system.integration;

import com.banco.bank_system.configuration.FixedClockTestConfiguration;
import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.entities.SavingsAccount;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.entities.helper.AccountFactory;
import com.banco.bank_system.infrastructure.database.mapper.AccountMapper;
import com.banco.bank_system.infrastructure.database.mapper.ClientMapper;
import com.banco.bank_system.infrastructure.database.mapper.TransactionMapper;
import com.banco.bank_system.infrastructure.database.sql.JpaAccountRepository;
import com.banco.bank_system.infrastructure.database.sql.JpaClientRepository;
import com.banco.bank_system.infrastructure.database.sql.JpaTransactionRepository;
import com.banco.bank_system.presentation.dto.request.transactions.DepositRequest;
import com.banco.bank_system.presentation.dto.request.transactions.TransferRequest;
import com.banco.bank_system.presentation.dto.request.transactions.WithdrawRequest;
import com.banco.bank_system.presentation.util.CurrencyFormatter;
import com.banco.bank_system.useCase.client.helper.ClientFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(FixedClockTestConfiguration.class)
class TransactionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JpaClientRepository clientRepository;

    @Autowired
    private JpaAccountRepository accountRepository;

    @Autowired
    private JpaTransactionRepository transactionRepository;

    @Autowired
    private Clock clock;

    @Test
    void shouldDeposit() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(ClientMapper.fromDomain(client));

        CheckingAccount account =
                AccountFactory.checking(client.getId(), clock);

        accountRepository.save(AccountMapper.fromDomain(account));

        DepositRequest request =
                new DepositRequest(
                        account.getAccountIdentity().branch(),
                        account.getAccountIdentity().accountNumber(),
                        "500"
                );

        String expectedAmount = CurrencyFormatter.format(Money.of("500"));

        mockMvc.perform(
                        post("/transactions/deposit")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.depositedAmount").value(expectedAmount));

        assertEquals(1, transactionRepository.count());
    }

    @Test
    void shouldWithdraw() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(ClientMapper.fromDomain(client));

        CheckingAccount account =
                AccountFactory.checking(client.getId(), clock);

        account.deposit(Money.of("1000"));

        accountRepository.save(AccountMapper.fromDomain(account));

        WithdrawRequest request =
                new WithdrawRequest(
                        account.getAccountIdentity().branch(),
                        account.getAccountIdentity().accountNumber(),
                        "300"
                );

        String expectedAmount = CurrencyFormatter.format(Money.of("300"));

        mockMvc.perform(
                        post("/transactions/withdraw")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.withdrawnAmount").value(expectedAmount));

        assertEquals(1, transactionRepository.count());
    }

    @Test
    void shouldTransfer() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(ClientMapper.fromDomain(client));

        CheckingAccount source =
                AccountFactory.checking(client.getId(), clock);

        source.deposit(Money.of("1000"));

        CheckingAccount destination =
                AccountFactory.checking(client.getId(), clock);

        accountRepository.save(AccountMapper.fromDomain(source));
        accountRepository.save(AccountMapper.fromDomain(destination));

        TransferRequest request =
                new TransferRequest(
                        source.getAccountIdentity().branch(),
                        source.getAccountIdentity().accountNumber(),
                        destination.getAccountIdentity().branch(),
                        destination.getAccountIdentity().accountNumber(),
                        "500"
                );

        String expectedAmount = CurrencyFormatter.format(Money.of("500"));

        mockMvc.perform(
                        post("/transactions/transfer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(expectedAmount));

        assertEquals(2, transactionRepository.count());
    }

    @Test
    void shouldReturnTransactions() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(ClientMapper.fromDomain(client));

        CheckingAccount account =
                AccountFactory.checking(client.getId(), clock);

        accountRepository.save(AccountMapper.fromDomain(account));

        transactionRepository.save(
                TransactionMapper.fromDomain(
                        Transaction.deposit(
                                account.getId(),
                                account.getAccountIdentity(),
                                Money.of("200"),
                                clock
                        )
                )
        );

        mockMvc.perform(
                        get("/transactions/{branch}/{number}",
                                account.getAccountIdentity().branch(),
                                account.getAccountIdentity().accountNumber())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].type").value("DEPOSIT"))
                .andExpect(jsonPath("$.content[0].amount").exists())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void shouldReturn404WhenDepositAccountDoesNotExist() throws Exception {

        DepositRequest request =
                new DepositRequest(
                        "01",
                        "123456-1",
                        "100"
                );

        mockMvc.perform(
                        post("/transactions/deposit")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code")
                        .value("ACCOUNT_NOT_FOUND"));
    }

    @Test
    void shouldReturn404WhenWithdrawAccountDoesNotExist() throws Exception {

        WithdrawRequest request =
                new WithdrawRequest(
                        "01",
                        "123456-1",
                        "100"
                );

        mockMvc.perform(
                        post("/transactions/withdraw")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code")
                        .value("ACCOUNT_NOT_FOUND"));
    }

    @Test
    void shouldReturn404WhenTransferSourceAccountDoesNotExist() throws Exception {

        TransferRequest request =
                new TransferRequest(
                        "01",
                        "123456-1",
                        "01",
                        "654321-1",
                        "100"
                );

        mockMvc.perform(
                        post("/transactions/transfer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code")
                        .value("ACCOUNT_NOT_FOUND"));
    }

    @Test
    void shouldReturn400WhenInsufficientBalance() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(ClientMapper.fromDomain(client));

        SavingsAccount account =
                AccountFactory.savings(client.getId(), clock);

        accountRepository.save(AccountMapper.fromDomain(account));

        WithdrawRequest request =
                new WithdrawRequest(
                        account.getAccountIdentity().branch(),
                        account.getAccountIdentity().accountNumber(),
                        "100"
                );

        mockMvc.perform(
                        post("/transactions/withdraw")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("INSUFFICIENT_BALANCE"));
    }
}