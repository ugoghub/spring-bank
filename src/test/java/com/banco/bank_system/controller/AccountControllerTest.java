package com.banco.bank_system.controller;

import com.banco.bank_system.application.account.dto.CreateAccountOutput;
import com.banco.bank_system.application.account.dto.GetBalanceOutput;
import com.banco.bank_system.application.account.dto.GetClientAccountOutput;
import com.banco.bank_system.application.account.dto.GetClientAccountsOutput;
import com.banco.bank_system.application.account.usecases.*;
import com.banco.bank_system.application.exception.AccountNotFoundException;
import com.banco.bank_system.application.exception.CannotRemoveAccountException;
import com.banco.bank_system.configuration.FixedClockTestConfiguration;
import com.banco.bank_system.domain.enums.AccountType;
import com.banco.bank_system.domain.valueobject.*;
import com.banco.bank_system.presentation.controller.AccountController;
import com.banco.bank_system.presentation.dto.request.account.CreateAccountRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@Import(FixedClockTestConfiguration.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateAccountUseCase createAccountUseCase;

    @MockitoBean
    private GetAccountBalanceUseCase getAccountBalanceUseCase;

    @MockitoBean
    private GetClientAccountsUseCase getClientAccountsUseCase;

    @MockitoBean
    private GetClientAccountUseCase getClientAccountUseCase;

    @MockitoBean
    private RemoveAccountUseCase removeAccountUseCase;


    private static final NumberFormat FORMAT =
            NumberFormat.getCurrencyInstance(
                    Locale.of("pt", "BR")
            );

    @Test
    void shouldCreateAccount() throws Exception {

        CreateAccountOutput output =
                new CreateAccountOutput(
                        AccountId.generate(),
                        ClientId.generate(),
                        new AccountIdentity("01", "123456-1"),
                        LocalDateTime.now(),
                        Money.ZERO
                );

        when(createAccountUseCase.execute(any(), any()))
                .thenReturn(output);

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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.branch").value("01"))
                .andExpect(jsonPath("$.accountNumber").value("123456-1"));

        verify(createAccountUseCase)
                .execute(any(CPF.class), any(AccountType.class));
    }

    @Test
    void shouldReturnBalance() throws Exception {

        GetBalanceOutput output =
                new GetBalanceOutput(
                        Money.of("1500")
                );

        when(getAccountBalanceUseCase.execute(any()))
                .thenReturn(output);

        mockMvc.perform(
                        get("/accounts/balance/01/123456-1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance")
                .value(FORMAT.format(BigDecimal.valueOf(1500))));

        verify(getAccountBalanceUseCase)
                .execute(any(AccountIdentity.class));
    }

    @Test
    void shouldReturnClientAccounts() throws Exception {

        GetClientAccountsOutput output =
                new GetClientAccountsOutput(
                        List.of(
                                new AccountIdentity("01", "123456-1"),
                                new AccountIdentity("01", "654321-1")
                        )
                );

        when(getClientAccountsUseCase.execute(any()))
                .thenReturn(output);

        mockMvc.perform(
                        get("/accounts/52998224725")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accounts.length()").value(2))
                .andExpect(jsonPath("$.accounts[0].branch").value("01"))
                .andExpect(jsonPath("$.accounts[0].accountNumber").value("123456-1"))
                .andExpect(jsonPath("$.accounts[1].accountNumber").value("654321-1"));

        verify(getClientAccountsUseCase)
                .execute(any(CPF.class));
    }

    @Test
    void shouldReturnClientAccount() throws Exception {

        GetClientAccountOutput output =
                new GetClientAccountOutput(
                        AccountId.generate(),
                        ClientId.generate(),
                        new AccountIdentity("01", "123456-1"),
                        LocalDateTime.now(),
                        Money.ZERO
                );

        when(getClientAccountUseCase.execute(any()))
                .thenReturn(output);

        mockMvc.perform(
                        get("/accounts/01/123456-1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.branch").value("01"))
                .andExpect(jsonPath("$.accountNumber").value("123456-1"));

        verify(getClientAccountUseCase)
                .execute(any(AccountIdentity.class));
    }

    @Test
    void shouldDeleteAccount() throws Exception {

        mockMvc.perform(
                        delete("/accounts/delete/01/123456-1")
                )
                .andExpect(status().isNoContent());

        verify(removeAccountUseCase)
                .execute(any(AccountIdentity.class));
    }

    @Test
    void shouldReturn404WhenAccountDoesNotExist() throws Exception {

        when(getClientAccountUseCase.execute(any()))
                .thenThrow(new AccountNotFoundException());

        mockMvc.perform(get("/accounts/01/123456-1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code")
                        .value("ACCOUNT_NOT_FOUND"))
                .andExpect(jsonPath("$.message")
                        .value("Conta não encontrada"));

        verify(getClientAccountUseCase)
                .execute(any(AccountIdentity.class));
    }

    @Test
    void shouldReturn404WhenBalanceAccountDoesNotExist() throws Exception {

        when(getAccountBalanceUseCase.execute(any()))
                .thenThrow(new AccountNotFoundException());

        mockMvc.perform(get("/accounts/balance/01/123456-1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code")
                        .value("ACCOUNT_NOT_FOUND"));

        verify(getAccountBalanceUseCase)
                .execute(any(AccountIdentity.class));
    }

    @Test
    void shouldReturn400WhenRemovingAccountWithBalance() throws Exception {

        doThrow(new CannotRemoveAccountException("Conta possui saldo ativo"))
                .when(removeAccountUseCase)
                .execute(any(AccountIdentity.class));

        mockMvc.perform(delete("/accounts/delete/01/123456-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("CANNOT_REMOVE_ACCOUNT"));

        verify(removeAccountUseCase)
                .execute(any(AccountIdentity.class));
    }

    @Test
    void shouldReturn500WhenUnexpectedError() throws Exception {

        when(getAccountBalanceUseCase.execute(any()))
                .thenThrow(new RuntimeException("Erro inesperado"));

        mockMvc.perform(get("/accounts/balance/01/123456-1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code")
                        .value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.message")
                        .value("Erro interno do servidor"));
    }
}
