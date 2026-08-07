package com.banco.bank_system.integration;

import com.banco.bank_system.configuration.FixedClockTestConfiguration;
import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.enums.AccountType;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.entities.helper.AccountFactory;
import com.banco.bank_system.infrastructure.database.entities.AccountEntity;
import com.banco.bank_system.infrastructure.database.mapper.AccountMapper;
import com.banco.bank_system.infrastructure.database.mapper.ClientMapper;
import com.banco.bank_system.infrastructure.database.sql.JpaAccountRepository;
import com.banco.bank_system.infrastructure.database.sql.JpaClientRepository;
import com.banco.bank_system.presentation.dto.request.account.CreateAccountRequest;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(value = FixedClockTestConfiguration.class)
class AccountIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Clock clock;

    @Autowired
    private JpaClientRepository clientRepository;

    @Autowired
    private JpaAccountRepository accountRepository;

    @Test
    void shouldCreateCheckingAccount() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(
                ClientMapper.fromDomain(client)
        );

        CreateAccountRequest request =
                new CreateAccountRequest(
                        client.getCpf().value(),
                        "CHECKING"
                );

        mockMvc.perform(
                        post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientId")
                        .value(client.getId().id().toString()));

        assertEquals(1, accountRepository.count());

        AccountEntity entity =
                accountRepository.findAll().getFirst();

        assertEquals(
                client.getId().id(),
                entity.getClientId()
        );

        assertEquals(
                AccountType.CHECKING,
                entity.getAccountType()
        );

        assertEquals(
                Money.ZERO.value(),
                entity.getBalance()
        );
    }

    @Test
    void shouldCreateSavingsAccount() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(
                ClientMapper.fromDomain(client)
        );

        CreateAccountRequest request =
                new CreateAccountRequest(
                        client.getCpf().value(),
                        "SAVINGS"
                );

        mockMvc.perform(
                        post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientId")
                        .value(client.getId().id().toString()));

        assertEquals(1, accountRepository.count());

        AccountEntity entity =
                accountRepository.findAll().getFirst();

        assertEquals(
                client.getId().id(),
                entity.getClientId()
        );

        assertEquals(
                AccountType.SAVINGS,
                entity.getAccountType()
        );
    }

    @Test
    void shouldReturnBalance() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(
                ClientMapper.fromDomain(client)
        );

        CheckingAccount account =
                AccountFactory.checking(client.getId(), clock);

        accountRepository.save(
                AccountMapper.fromDomain(account)
        );

        mockMvc.perform(
                        get("/accounts/balance/{branch}/{account}",
                                account.getAccountIdentity().branch(),
                                account.getAccountIdentity().accountNumber())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(CurrencyFormatter.format(account.getBalance())));
    }

    @Test
    void shouldReturnClientAccounts() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(
                ClientMapper.fromDomain(client)
        );

        accountRepository.save(
                AccountMapper.fromDomain(
                        AccountFactory.checking(client.getId(), clock)
                )
        );

        accountRepository.save(
                AccountMapper.fromDomain(
                        AccountFactory.savings(client.getId(), clock)
                )
        );

        mockMvc.perform(
                        get("/accounts/" + client.getCpf().value())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accounts.length()").value(2));
    }

    @Test
    void shouldReturnSingleAccount() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(
                ClientMapper.fromDomain(client)
        );

        CheckingAccount account =
                AccountFactory.checking(client.getId(), clock);

        accountRepository.save(
                AccountMapper.fromDomain(account)
        );

        mockMvc.perform(
                        get("/accounts/{branch}/{number}",
                                account.getAccountIdentity().branch(),
                                account.getAccountIdentity().accountNumber())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branch")
                        .value(account.getAccountIdentity().branch()))
                .andExpect(jsonPath("$.accountNumber")
                        .value(account.getAccountIdentity().accountNumber()));
    }

    @Test
    void shouldDeleteAccount() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(
                ClientMapper.fromDomain(client)
        );

        CheckingAccount account =
                AccountFactory.checking(client.getId(), clock);

        accountRepository.save(
                AccountMapper.fromDomain(account)
        );

        mockMvc.perform(
                        delete("/accounts/{branch}/{number}",
                                account.getAccountIdentity().branch(),
                                account.getAccountIdentity().accountNumber())
                )
                .andExpect(status().isNoContent());

        assertTrue(accountRepository.findAll().isEmpty());
    }

    @Test
    void shouldReturn404WhenClientDoesNotExist() throws Exception {

        CreateAccountRequest request =
                new CreateAccountRequest(
                        "52998224725",
                        "CHECKING"
                );

        mockMvc.perform(
                        post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code")
                        .value("CLIENT_NOT_FOUND"));
    }

    @Test
    void shouldReturn404WhenAccountDoesNotExist() throws Exception {

        mockMvc.perform(
                        get("/accounts/01/123456-1")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code")
                        .value("ACCOUNT_NOT_FOUND"));
    }

    @Test
    void shouldReturn404WhenBalanceAccountDoesNotExist() throws Exception {

        mockMvc.perform(
                        get("/accounts/balance/01/123456-1")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code")
                        .value("ACCOUNT_NOT_FOUND"));
    }

    @Test
    void shouldReturn400WhenDeletingAccountWithBalance() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(
                ClientMapper.fromDomain(client)
        );

        CheckingAccount account =
                AccountFactory.checking(client.getId(), clock);

        account.deposit(Money.of("100"));

        accountRepository.save(
                AccountMapper.fromDomain(account)
        );

        mockMvc.perform(
                        delete("/accounts/{branch}/{number}",
                                account.getAccountIdentity().branch(),
                                account.getAccountIdentity().accountNumber())
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("CANNOT_REMOVE_ACCOUNT"));
    }

    @Test
    void shouldReturn400WhenCpfIsInvalid() throws Exception {

        CreateAccountRequest request =
                new CreateAccountRequest(
                        "52998224726",
                        "CHECKING"
                );

        mockMvc.perform(
                        post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("INVALID_CPF"));
    }

    @Test
    void shouldReturn400WhenAccountTypeIsInvalid() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(
                ClientMapper.fromDomain(client)
        );

        CreateAccountRequest request =
                new CreateAccountRequest(
                        client.getCpf().value(),
                        "INVALID"
                );

        mockMvc.perform(
                        post("/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("INVALID_ACCOUNT_TYPE"));
    }


}