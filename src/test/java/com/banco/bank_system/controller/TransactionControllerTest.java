package com.banco.bank_system.controller;

import com.banco.bank_system.application.exception.AccountNotFoundException;
import com.banco.bank_system.application.exception.InvalidTransferException;
import com.banco.bank_system.application.transaction.dto.DepositOutput;
import com.banco.bank_system.application.transaction.dto.TransactionDTO;
import com.banco.bank_system.application.transaction.dto.TransferOutput;
import com.banco.bank_system.application.transaction.dto.WithdrawOutput;
import com.banco.bank_system.application.transaction.usecases.DepositUseCase;
import com.banco.bank_system.application.transaction.usecases.GetTransactionsUseCase;
import com.banco.bank_system.application.transaction.usecases.TransferUseCase;
import com.banco.bank_system.application.transaction.usecases.WithdrawUseCase;
import com.banco.bank_system.configuration.FixedClockTestConfiguration;
import com.banco.bank_system.domain.enums.TransactionType;
import com.banco.bank_system.domain.exception.InsufficientBalanceException;
import com.banco.bank_system.domain.valueobject.*;
import com.banco.bank_system.presentation.controller.TransactionController;
import com.banco.bank_system.presentation.dto.request.transactions.DepositRequest;
import com.banco.bank_system.presentation.dto.request.transactions.TransferRequest;
import com.banco.bank_system.presentation.dto.request.transactions.WithdrawRequest;
import com.banco.bank_system.presentation.util.CurrencyFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@Import(FixedClockTestConfiguration.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DepositUseCase depositUseCase;

    @MockitoBean
    private WithdrawUseCase withdrawUseCase;

    @MockitoBean
    private TransferUseCase transferUseCase;

    @MockitoBean
    private GetTransactionsUseCase getTransactionsUseCase;

    @Test
    void shouldDeposit() throws Exception {

        DepositOutput output =
                new DepositOutput(
                        new AccountId(UUID.randomUUID()),
                        Money.of("500"),
                        Money.of("1500"),
                        new TransactionId(UUID.randomUUID()),
                        LocalDateTime.of(2026,1,10,10,0)
                );

        when(depositUseCase.execute(any(), any()))
                .thenReturn(output);

        DepositRequest request =
                new DepositRequest(
                        "01",
                        "123456-1",
                        "500"
                );

        mockMvc.perform(
                        post("/transactions/deposits")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.depositedAmount").value(CurrencyFormatter.format(output.depositedAmount())))
                .andExpect(jsonPath("$.newBalance").value(CurrencyFormatter.format(output.newBalance())));

        verify(depositUseCase)
                .execute(
                        any(AccountIdentity.class),
                        any(Money.class)
                );
    }

    @Test
    void shouldWithdraw() throws Exception {

        WithdrawOutput output =
                new WithdrawOutput(
                        new AccountId(UUID.randomUUID()),
                        Money.of("300"),
                        Money.of("700"),
                        new TransactionId(UUID.randomUUID()),
                        LocalDateTime.of(2026,1,10,10,0)
                );

        when(withdrawUseCase.execute(any(), any()))
                .thenReturn(output);

        WithdrawRequest request =
                new WithdrawRequest(
                        "01",
                        "123456-1",
                        "300"
                );

        mockMvc.perform(
                        post("/transactions/withdrawals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.withdrawnAmount").exists())
                .andExpect(jsonPath("$.newBalance").exists());

        verify(withdrawUseCase)
                .execute(
                        any(AccountIdentity.class),
                        any(Money.class)
                );
    }

    @Test
    void shouldTransfer() throws Exception {

        TransferOutput output =
                new TransferOutput(
                        new OperationId(UUID.randomUUID()),
                        new AccountIdentity("01", "123456-1"),
                        new AccountIdentity("01", "654321-1"),
                        Money.of("500"),
                        LocalDateTime.of(2026, 1, 10, 10, 0)
                );

        when(transferUseCase.execute(any(), any(), any()))
                .thenReturn(output);

        TransferRequest request =
                new TransferRequest(
                        "01",
                        "123456-1",
                        "01",
                        "654321-1",
                        "500"
                );

        String expectedAmount = CurrencyFormatter.format(Money.of("500"));

        mockMvc.perform(
                        post("/transactions/transfers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operationId").exists())
                .andExpect(jsonPath("$.source_branch").value("01"))
                .andExpect(jsonPath("$.source_accountNumber").value("123456-1"))
                .andExpect(jsonPath("$.destination_branch").value("01"))
                .andExpect(jsonPath("$.destination_accountNumber").value("654321-1"))
                .andExpect(jsonPath("$.amount").value(expectedAmount))
                .andExpect(jsonPath("$.transactionDate")
                        .value("10/01/2026 10:00:00"));

        verify(transferUseCase)
                .execute(
                        any(AccountIdentity.class),
                        any(AccountIdentity.class),
                        any(Money.class)
                );
    }


    @Test
    void shouldReturnTransactions() throws Exception {

        Page<TransactionDTO> output =
                new PageImpl<>(
                        List.of(
                                new TransactionDTO(
                                        new TransactionId(UUID.randomUUID()),
                                        OperationId.generate(),
                                        TransactionType.DEPOSIT,
                                        Money.of("500"),
                                        null,
                                        null,
                                        "01",
                                        "123456-1",
                                        LocalDateTime.of(2026, 1, 10, 10, 0)
                                ),
                                new TransactionDTO(
                                        new TransactionId(UUID.randomUUID()),
                                        OperationId.generate(),
                                        TransactionType.WITHDRAW,
                                        Money.of("200"),
                                        "01",
                                        "123456-1",
                                        null,
                                        null,
                                        LocalDateTime.of(2026, 1, 10, 11, 0)
                                )
                        ),
                        PageRequest.of(0, 10),
                        2
                );

        when(getTransactionsUseCase.execute(
                any(AccountIdentity.class),
                anyInt(),
                anyInt()))
                .thenReturn(output);

        mockMvc.perform(
                        get("/transactions/01/123456-1")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.content[0].type").value("DEPOSIT"))
                .andExpect(jsonPath("$.content[0].destination_branch").value("01"))
                .andExpect(jsonPath("$.content[0].destination_accountNumber").value("123456-1"))

                .andExpect(jsonPath("$.content[1].type").value("WITHDRAW"))
                .andExpect(jsonPath("$.content[1].source_branch").value("01"))
                .andExpect(jsonPath("$.content[1].source_accountNumber").value("123456-1"))

                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.numberOfElements").value(2))
                .andExpect(jsonPath("$.empty").value(false));

        verify(getTransactionsUseCase)
                .execute(
                        any(AccountIdentity.class),
                        eq(0),
                        eq(10)
                );
    }

    @Test
    void shouldReturn404WhenDepositingIntoNonExistingAccount() throws Exception {

        when(depositUseCase.execute(any(), any()))
                .thenThrow(new AccountNotFoundException());

        DepositRequest request =
                new DepositRequest(
                        "01",
                        "123456-1",
                        "500"
                );

        mockMvc.perform(
                        post("/transactions/deposits")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code")
                        .value("ACCOUNT_NOT_FOUND"));

        verify(depositUseCase)
                .execute(any(AccountIdentity.class), any(Money.class));
    }

    @Test
    void shouldReturn400WhenBalanceIsInsufficient() throws Exception {

        when(withdrawUseCase.execute(any(), any()))
                .thenThrow(new InsufficientBalanceException("Saldo insuficiente"));

        WithdrawRequest request =
                new WithdrawRequest(
                        "01",
                        "123456-1",
                        "500"
                );

        mockMvc.perform(
                        post("/transactions/withdrawals")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("INSUFFICIENT_BALANCE"));

        verify(withdrawUseCase)
                .execute(any(AccountIdentity.class), any(Money.class));
    }

    @Test
    void shouldReturn400WhenTransferIsInvalid() throws Exception {

        when(transferUseCase.execute(any(), any(), any()))
                .thenThrow(new InvalidTransferException("Transferência inválida"));

        TransferRequest request =
                new TransferRequest(
                        "01",
                        "123456-1",
                        "01",
                        "123456-1",
                        "100"
                );

        mockMvc.perform(
                        post("/transactions/transfers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("INVALID_TRANSFER"));

        verify(transferUseCase)
                .execute(any(), any(), any());
    }

    @Test
    void shouldReturn400WhenAccountIdentityIsInvalid() throws Exception {

        DepositRequest request =
                new DepositRequest(
                        "1",
                        "123456-1",
                        "500"
                );

        mockMvc.perform(
                        post("/transactions/deposits")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("INVALID_ACCOUNT_IDENTITY"));

        verifyNoInteractions(depositUseCase);
    }

    @Test
    void shouldReturn400WhenBranchIsBlank() throws Exception {

        DepositRequest request =
                new DepositRequest(
                        "",
                        "123456-1",
                        "500"
                );

        mockMvc.perform(
                        post("/transactions/deposits")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("VALIDATION_ERROR"));

        verifyNoInteractions(depositUseCase);
    }
}
