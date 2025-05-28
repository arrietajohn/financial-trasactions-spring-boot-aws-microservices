package com.cobre.accountservice.application.usecase;

import com.cobre.accountservice.application.usecases.create.CreateAccountHandler;
import com.cobre.accountservice.application.dto.CreateAccountCommand;
import com.cobre.accountservice.application.dto.CreateAccountResponse;
import com.cobre.accountservice.domain.model.Account;
import com.cobre.accountservice.domain.port.out.IAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateAccountHandlerTest {

    @Mock
    private IAccountRepository accountRepository;

    @InjectMocks
    private CreateAccountHandler accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateAccountSuccessfully() {
        CreateAccountCommand request = new CreateAccountCommand("John Doe", "john@example.com", BigDecimal.valueOf(100));

        CreateAccountResponse response = accountService.handle(request);

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());

        Account savedAccount = captor.getValue();
        assertEquals("John Doe", savedAccount.getName());
        assertEquals("john@example.com", savedAccount.getEmail());
        assertEquals(BigDecimal.valueOf(100), savedAccount.getBalance());

        assertNotNull(response);
    }

    @Test
    void shouldThrowExceptionWhenBalanceIsNegative() {
        CreateAccountCommand request = new CreateAccountCommand("Jane Doe", "jane@example.com", BigDecimal.valueOf(-50));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                accountService.handle(request)
        );

        assertEquals("Initial balance must be non-negative", exception.getMessage());
    }
}
