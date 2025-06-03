package com.cobre.accountservice.application.usecase;

import com.cobre.accountservice.application.dto.CreateAccountCommand;
import com.cobre.accountservice.application.port.in.create.ICreateAccountUseCase;
import com.cobre.accountservice.domain.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SomeServiceUsingCreateAccountUseCaseTest {

    @Mock
    private ICreateAccountUseCase createAccountUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCallCreateAccountUseCaseSuccessfully() {
        CreateAccountCommand request = new CreateAccountCommand("John Doe", "john@example.com", BigDecimal.valueOf(100));
        Account expectedAccount = new Account("John Doe", "john@example.com", BigDecimal.valueOf(100)); // La cuenta que esperamos que el mock retorne
        when(createAccountUseCase.handle(any(CreateAccountCommand.class))).thenReturn(expectedAccount);

        Account newAccount = createAccountUseCase.handle(request);

        verify(createAccountUseCase, times(1)).handle(request);

        assertNotNull(newAccount, "La cuenta devuelta no debe ser nula");
        assertEquals("John Doe", newAccount.getName(), "El nombre de la cuenta devuelta no coincide");
        assertEquals("john@example.com", newAccount.getEmail(), "El email de la cuenta devuelta no coincide");
        assertEquals(BigDecimal.valueOf(100), newAccount.getBalance(), "El balance de la cuenta devuelta no coincide");
        assertEquals(expectedAccount, newAccount, "La cuenta devuelta debe ser la misma que la esperada");
    }

    @Test
    void shouldHandleExceptionFromCreateAccountUseCase() {
        CreateAccountCommand request = new CreateAccountCommand("Jane Doe", "jane@example.com", BigDecimal.valueOf(-50));

        when(createAccountUseCase.handle(any(CreateAccountCommand.class)))
                .thenThrow(new IllegalArgumentException("Initial balance must be non-negative"));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                createAccountUseCase.handle(request)
        );

        assertEquals("Initial balance must be non-negative", exception.getMessage(), "El mensaje de error no coincide");

        verify(createAccountUseCase, times(1)).handle(request);
    }
}
