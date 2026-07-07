package com.banco.bank_system.useCase.clientUseCaseTests;

import com.banco.bank_system.application.client.dto.CreateClientOutput;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.client.usecases.CreateClientUseCase;
import com.banco.bank_system.application.exception.CpfAlreadyExistsException;
import com.banco.bank_system.application.exception.EmailAlreadyExistsException;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateClientUseCaseTest {

    @Mock
    private ClientRepositoryPort clientRepository;

    @InjectMocks
    private CreateClientUseCase useCase;

    @Test
    void shouldCreateClient() {

        PersonName name = new PersonName("Hugo Silva");
        CPF cpf = new CPF("52998224725");
        Email email = new Email("hugo@gmail.com");

        when(clientRepository.existsByCpf(cpf))
                .thenReturn(false);

        when(clientRepository.existsByEmail(email))
                .thenReturn(false);

        CreateClientOutput output =
                useCase.execute(name, cpf, email);

        verify(clientRepository).save(any(Client.class));

        assertEquals(name, output.name());
        assertEquals(cpf, output.cpf());
        assertEquals(email, output.email());
    }

    @Test
    void shouldThrowExceptionWhenCpfAlreadyExists() {

        CPF cpf = new CPF("52998224725");

        when(clientRepository.existsByCpf(cpf))
                .thenReturn(true);

        assertThrows(
                CpfAlreadyExistsException.class,
                () -> useCase.execute(
                        new PersonName("Hugo"),
                        cpf,
                        new Email("a@gmail.com")
                )
        );

        verify(clientRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        Email email = new Email("a@gmail.com");

        when(clientRepository.existsByCpf(any()))
                .thenReturn(false);

        when(clientRepository.existsByEmail(email))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> useCase.execute(
                        new PersonName("Hugo"),
                        new CPF("52998224725"),
                        email
                )
        );

        verify(clientRepository, never())
                .save(any());
    }

    @Test
    void shouldValidateCpfBeforeEmail() {

        when(clientRepository.existsByCpf(any()))
                .thenReturn(true);

        assertThrows(
                CpfAlreadyExistsException.class,
                () -> useCase.execute(
                        new PersonName("Hugo"),
                        new CPF("52998224725"),
                        new Email("a@gmail.com")
                )
        );

        verify(clientRepository)
                .existsByCpf(any());

        verify(clientRepository, never())
                .existsByEmail(any());
    }

    @Test
    void shouldSaveOnlyOnce() {

        when(clientRepository.existsByCpf(any()))
                .thenReturn(false);

        when(clientRepository.existsByEmail(any()))
                .thenReturn(false);

        useCase.execute(
                new PersonName("Hugo"),
                new CPF("52998224725"),
                new Email("a@gmail.com")
        );

        verify(clientRepository, times(1))
                .save(any(Client.class));

        verifyNoMoreInteractions(clientRepository);
    }
}
