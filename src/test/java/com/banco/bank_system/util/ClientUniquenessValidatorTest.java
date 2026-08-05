package com.banco.bank_system.util;

import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.client.util.ClientUniquenessValidator;
import com.banco.bank_system.application.exception.CpfAlreadyExistsException;
import com.banco.bank_system.application.exception.EmailAlreadyExistsException;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientUniquenessValidatorTest {

    @Mock
    private ClientRepositoryPort clientRepository;

    @InjectMocks
    private ClientUniquenessValidator validator;

    private final CPF cpf = new CPF("52998224725");
    private final Email email = new Email("hugo@gmail.com");

    @Test
    void shouldValidateUniqueClient() {

        when(clientRepository.existsByCpf(cpf))
                .thenReturn(false);

        when(clientRepository.existsByEmail(email))
                .thenReturn(false);

        assertDoesNotThrow(() ->
                validator.validate(cpf, email)
        );

        verify(clientRepository)
                .existsByCpf(cpf);

        verify(clientRepository)
                .existsByEmail(email);

        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    void shouldThrowExceptionWhenCpfAlreadyExists() {

        when(clientRepository.existsByCpf(cpf))
                .thenReturn(true);

        assertThrows(
                CpfAlreadyExistsException.class,
                () -> validator.validate(cpf, email)
        );

        verify(clientRepository)
                .existsByCpf(cpf);

        verify(clientRepository, never())
                .existsByEmail(any());

        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        when(clientRepository.existsByCpf(cpf))
                .thenReturn(false);

        when(clientRepository.existsByEmail(email))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> validator.validate(cpf, email)
        );

        verify(clientRepository)
                .existsByCpf(cpf);

        verify(clientRepository)
                .existsByEmail(email);

        verifyNoMoreInteractions(clientRepository);
    }
}
