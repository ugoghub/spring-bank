package com.banco.bank_system.controller;

import com.banco.bank_system.application.client.dto.CreateClientOutput;
import com.banco.bank_system.application.client.dto.GetClientDataOutput;
import com.banco.bank_system.application.client.usecases.*;
import com.banco.bank_system.application.exception.ClientNotFoundException;
import com.banco.bank_system.application.exception.CpfAlreadyExistsException;
import com.banco.bank_system.application.exception.EmailAlreadyExistsException;
import com.banco.bank_system.configuration.FixedClockTestConfiguration;
import com.banco.bank_system.domain.exception.InvalidClientChangeException;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.ClientId;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;
import com.banco.bank_system.presentation.controller.ClientController;
import com.banco.bank_system.presentation.dto.request.client.ChangeClientEmailRequest;
import com.banco.bank_system.presentation.dto.request.client.ChangeClientNameRequest;
import com.banco.bank_system.presentation.dto.request.client.CreateClientRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
@Import(FixedClockTestConfiguration.class)
class ClientControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private CreateClientUseCase createClientUseCase;

        @MockitoBean
        private GetClientDataUseCase getClientDataUseCase;

        @MockitoBean
        private ChangeClientNameUseCase changeClientNameUseCase;

        @MockitoBean
        private ChangeClientEmailUseCase changeClientEmailUseCase;

        @MockitoBean
        private RemoveClientUseCase removeClientUseCase;



    @Test
    void shouldCreateClient() throws Exception {

        CreateClientOutput output =
                new CreateClientOutput(
                        new ClientId(UUID.randomUUID()),
                        new PersonName("Hugo"),
                        new CPF("52998224725"),
                        new Email("hugo@gmail.com")
                );

        when(createClientUseCase.execute(any(), any(), any()))
                .thenReturn(output);

        CreateClientRequest request =
                new CreateClientRequest(
                        "Hugo",
                        "52998224725",
                        "hugo@gmail.com"
                );

        mockMvc.perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Hugo"))
                .andExpect(jsonPath("$.cpf").value("52998224725"))
                .andExpect(jsonPath("$.email").value("hugo@gmail.com"));

        verify(createClientUseCase)
                .execute(
                        any(PersonName.class),
                        any(CPF.class),
                        any(Email.class)
                );
    }

    @Test
    void shouldReturnClientData() throws Exception {

        GetClientDataOutput output =
                new GetClientDataOutput(
                        new PersonName("Hugo"),
                        new CPF("52998224725"),
                        new Email("hugo@gmail.com")
                );

        when(getClientDataUseCase.execute(any()))
                .thenReturn(output);

        mockMvc.perform(get("/clients/52998224725"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hugo"))
                .andExpect(jsonPath("$.cpf").value("52998224725"))
                .andExpect(jsonPath("$.email").value("hugo@gmail.com"));

        verify(getClientDataUseCase)
                .execute(any(CPF.class));
    }

    @Test
    void shouldChangeClientName() throws Exception {

        GetClientDataOutput output =
                new GetClientDataOutput(
                        new PersonName("Novo Nome"),
                        new CPF("52998224725"),
                        new Email("hugo@gmail.com")
                );

        when(changeClientNameUseCase.execute(any(), any()))
                .thenReturn(output);

        ChangeClientNameRequest request =
                new ChangeClientNameRequest("Novo Nome");

        mockMvc.perform(
                        patch("/clients/52998224725/name")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Novo Nome"));

        verify(changeClientNameUseCase)
                .execute(any(CPF.class), any(PersonName.class));
    }

    @Test
    void shouldChangeClientEmail() throws Exception {

        GetClientDataOutput output =
                new GetClientDataOutput(
                        new PersonName("Hugo"),
                        new CPF("52998224725"),
                        new Email("novo@gmail.com")
                );

        when(changeClientEmailUseCase.execute(any(), any()))
                .thenReturn(output);

        ChangeClientEmailRequest request =
                new ChangeClientEmailRequest("novo@gmail.com");

        mockMvc.perform(
                        patch("/clients/52998224725/email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("novo@gmail.com"));

        verify(changeClientEmailUseCase)
                .execute(any(CPF.class), any(Email.class));
    }

    @Test
    void shouldRemoveClient() throws Exception {

        mockMvc.perform(delete("/clients/52998224725"))
                .andExpect(status().isNoContent());

        verify(removeClientUseCase)
                .execute(any(CPF.class));
    }

    @Test
    void shouldReturn409WhenCpfAlreadyExists() throws Exception {

        when(createClientUseCase.execute(any(), any(), any()))
                .thenThrow(new CpfAlreadyExistsException());

        CreateClientRequest request =
                new CreateClientRequest(
                        "Hugo",
                        "52998224725",
                        "hugo@gmail.com"
                );

        mockMvc.perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("CPF_ALREADY_EXISTS"));
    }

    @Test
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {

        when(createClientUseCase.execute(any(), any(), any()))
                .thenThrow(new EmailAlreadyExistsException());

        CreateClientRequest request =
                new CreateClientRequest(
                        "Hugo",
                        "52998224725",
                        "hugo@gmail.com"
                );

        mockMvc.perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("EMAIL_ALREADY_EXISTS"));
    }

    @Test
    void shouldReturn404WhenClientDoesNotExist() throws Exception {

        when(getClientDataUseCase.execute(any()))
                .thenThrow(new ClientNotFoundException());

        mockMvc.perform(get("/clients/52998224725"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("CLIENT_NOT_FOUND"));
    }

    @Test
    void shouldReturn400WhenChangingToSameName() throws Exception {

        when(changeClientNameUseCase.execute(any(), any()))
                .thenThrow(new InvalidClientChangeException("Novo nome é igual ao nome atual"));

        ChangeClientNameRequest request =
                new ChangeClientNameRequest("Hugo");

        mockMvc.perform(
                        patch("/clients/52998224725/name")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_CLIENT_CHANGE"));
    }

    @Test
    void shouldReturn400WhenChangingToSameEmail() throws Exception {

        when(changeClientEmailUseCase.execute(any(), any()))
                .thenThrow(new InvalidClientChangeException("Novo email é igual ao email atual"));

        ChangeClientEmailRequest request =
                new ChangeClientEmailRequest("hugo@gmail.com");

        mockMvc.perform(
                        patch("/clients/52998224725/email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_CLIENT_CHANGE"));
    }

    @Test
    void shouldReturn404WhenRemovingNonExistingClient() throws Exception {

        doThrow(new ClientNotFoundException())
                .when(removeClientUseCase)
                .execute(any());

        mockMvc.perform(delete("/clients/52998224725"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("CLIENT_NOT_FOUND"));
    }

    @Test
    void shouldReturn400WhenNameIsBlank() throws Exception {

        CreateClientRequest request =
                new CreateClientRequest(
                        "",
                        "52998224725",
                        "hugo@gmail.com"
                );

        mockMvc.perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("VALIDATION_ERROR"));

        verifyNoInteractions(createClientUseCase);
    }

    @Test
    void shouldReturn400WhenCpfIsBlank() throws Exception {

        CreateClientRequest request =
                new CreateClientRequest(
                        "Hugo",
                        "",
                        "hugo@gmail.com"
                );

        mockMvc.perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("VALIDATION_ERROR"));

        verifyNoInteractions(createClientUseCase);
    }

    @Test
    void shouldReturn400WhenEmailIsBlank() throws Exception {

        CreateClientRequest request =
                new CreateClientRequest(
                        "Hugo",
                        "52998224725",
                        ""
                );

        mockMvc.perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("VALIDATION_ERROR"));

        verifyNoInteractions(createClientUseCase);
    }
    @Test
    void shouldReturn400WhenCpfIsInvalid() throws Exception {

        CreateClientRequest request =
                new CreateClientRequest(
                        "Hugo",
                        "52998224726",
                        "hugo@gmail.com"
                );

        mockMvc.perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("INVALID_CPF"));

        verifyNoInteractions(createClientUseCase);
    }

    @Test
    void shouldReturn400WhenEmailIsInvalid() throws Exception {

        CreateClientRequest request =
                new CreateClientRequest(
                        "Hugo",
                        "52998224725",
                        "email-invalido"
                );

        mockMvc.perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("INVALID_EMAIL"));

        verifyNoInteractions(createClientUseCase);
    }

    @Test
    void shouldReturn400WhenPersonNameObjectIsInvalid() throws Exception {

        CreateClientRequest request =
                new CreateClientRequest(
                        "A",
                        "52998224725",
                        "hugo@gmail.com"
                );

        mockMvc.perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("INVALID_NAME"));

        verifyNoInteractions(createClientUseCase);
    }

    @Test
    void shouldReturn400WhenChangingNameToBlank() throws Exception {

        ChangeClientNameRequest request =
                new ChangeClientNameRequest("");

        mockMvc.perform(
                        patch("/clients/52998224725/name")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("VALIDATION_ERROR"));

        verifyNoInteractions(changeClientNameUseCase);
    }

    @Test
    void shouldReturn400WhenChangingToInvalidName() throws Exception {

        ChangeClientNameRequest request =
                new ChangeClientNameRequest("A");

        mockMvc.perform(
                        patch("/clients/52998224725/name")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("INVALID_NAME"));

        verifyNoInteractions(changeClientNameUseCase);
    }

    @Test
    void shouldReturn400WhenChangingEmailToBlank() throws Exception {

        ChangeClientEmailRequest request =
                new ChangeClientEmailRequest("");

        mockMvc.perform(
                        patch("/clients/52998224725/email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("VALIDATION_ERROR"));

        verifyNoInteractions(changeClientEmailUseCase);
    }

    @Test
    void shouldReturn400WhenChangingToInvalidEmail() throws Exception {

        ChangeClientEmailRequest request =
                new ChangeClientEmailRequest("email-invalido");

        mockMvc.perform(
                        patch("/clients/52998224725/email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("INVALID_EMAIL"));

        verifyNoInteractions(changeClientEmailUseCase);
    }
}

