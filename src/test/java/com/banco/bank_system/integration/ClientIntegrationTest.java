package com.banco.bank_system.integration;

import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;
import com.banco.bank_system.infrastructure.database.entities.ClientEntity;
import com.banco.bank_system.infrastructure.database.mapper.ClientMapper;
import com.banco.bank_system.infrastructure.database.sql.JpaClientRepository;
import com.banco.bank_system.presentation.dto.request.client.ChangeClientEmailRequest;
import com.banco.bank_system.presentation.dto.request.client.ChangeClientNameRequest;
import com.banco.bank_system.presentation.dto.request.client.CreateClientRequest;
import com.banco.bank_system.useCase.client.helper.ClientFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ClientIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JpaClientRepository clientRepository;

    @Test
    void shouldCreateClient() throws Exception {

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

        assertEquals(1, clientRepository.count());

        ClientEntity entity =
                clientRepository.findByCpf("52998224725")
                        .orElseThrow();

        assertEquals("Hugo", entity.getName());
        assertEquals("52998224725", entity.getCpf());
        assertEquals("hugo@gmail.com", entity.getEmail());
    }
    @Test
    void shouldReturnClientData() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(
                ClientMapper.fromDomain(client)
        );

        mockMvc.perform(
                        get("/clients/" + client.getCpf().value())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value(client.getName().value()))
                .andExpect(jsonPath("$.cpf")
                        .value(client.getCpf().value()))
                .andExpect(jsonPath("$.email")
                        .value(client.getEmail().value()));
    }

    @Test
    void shouldChangeClientName() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(
                ClientMapper.fromDomain(client)
        );

        ChangeClientNameRequest request =
                new ChangeClientNameRequest("Novo Nome");

        mockMvc.perform(
                        patch("/clients/" + client.getCpf().value() + "/name")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Novo Nome"));

        ClientEntity updated =
                clientRepository.findByCpf(client.getCpf().value()).orElseThrow();

        assertEquals("Novo Nome", updated.getName());
    }

    @Test
    void shouldChangeClientEmail() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(
                ClientMapper.fromDomain(client)
        );

        ChangeClientEmailRequest request =
                new ChangeClientEmailRequest("novo@gmail.com");

        mockMvc.perform(
                        patch("/clients/" + client.getCpf().value() + "/email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("novo@gmail.com"));

        ClientEntity updated =
                clientRepository.findByCpf(client.getCpf().value()).orElseThrow();

        assertEquals(
                "novo@gmail.com",
                updated.getEmail()
        );
    }

    @Test
    void shouldDeleteClient() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(
                ClientMapper.fromDomain(client)
        );

        mockMvc.perform(
                        delete("/clients/" + client.getCpf().value())
                )
                .andExpect(status().isNoContent());

        assertFalse(
                clientRepository.existsByCpf(client.getCpf().value())
        );
    }

    @Test
    void shouldReturn404WhenClientDoesNotExist() throws Exception {

        mockMvc.perform(
                        get("/clients/52998224725")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code")
                        .value("CLIENT_NOT_FOUND"));
    }

    @Test
    void shouldReturn409WhenCpfAlreadyExists() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(
                ClientMapper.fromDomain(client)
        );

        CreateClientRequest request =
                new CreateClientRequest(
                        "Outro Nome",
                        client.getCpf().value(),
                        "outro@email.com"
                );

        mockMvc.perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code")
                        .value("CPF_ALREADY_EXISTS"));
    }

    @Test
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(
                ClientMapper.fromDomain(client)
        );

        CreateClientRequest request =
                new CreateClientRequest(
                        "Outro Nome",
                        "11144477735",
                        client.getEmail().value()
                );

        mockMvc.perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code")
                        .value("EMAIL_ALREADY_EXISTS"));
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
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("INVALID_CPF"));
    }

    @Test
    void shouldReturn400WhenNameIsInvalid() throws Exception {

        CreateClientRequest request =
                new CreateClientRequest(
                        "",
                        "52998224725",
                        "hugo@gmail.com"
                );

        mockMvc.perform(
                        post("/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("INVALID_NAME"));
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
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("INVALID_EMAIL"));
    }

    @Test
    void shouldReturn400WhenChangingToSameName() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(
                ClientMapper.fromDomain(client)
        );

        ChangeClientNameRequest request =
                new ChangeClientNameRequest(
                        client.getName().value()
                );

        mockMvc.perform(
                        patch("/clients/" + client.getCpf().value() + "/name")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("INVALID_CLIENT_CHANGE"));
    }

    @Test
    void shouldReturn400WhenChangingToSameEmail() throws Exception {

        Client client = ClientFactory.create();

        clientRepository.save(
                ClientMapper.fromDomain(client)
        );

        ChangeClientEmailRequest request =
                new ChangeClientEmailRequest(
                        client.getEmail().value()
                );

        mockMvc.perform(
                        patch("/clients/" + client.getCpf().value() + "/email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value("INVALID_CLIENT_CHANGE"));
    }

    @Test
    void shouldReturn409WhenChangingToExistingEmail() throws Exception {

        Client firstClient = ClientFactory.create();

        Client secondClient = Client.create(
                new PersonName("Outro Cliente"),
                new CPF("11144477735"),
                new Email("outro@gmail.com")
        );

        clientRepository.save(
                ClientMapper.fromDomain(firstClient)
        );

        clientRepository.save(
                ClientMapper.fromDomain(secondClient)
        );

        ChangeClientEmailRequest request =
                new ChangeClientEmailRequest(
                        secondClient.getEmail().value()
                );

        mockMvc.perform(
                        patch("/clients/" + firstClient.getCpf().value() + "/email")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code")
                        .value("EMAIL_ALREADY_EXISTS"));
    }
}
