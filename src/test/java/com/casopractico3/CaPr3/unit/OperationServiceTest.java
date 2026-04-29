package com.casopractico3.CaPr3.unit;

import com.casopractico3.CaPr3.dto.MovementDTO;
import com.casopractico3.CaPr3.exception.AccountNotFoundException;
import com.casopractico3.CaPr3.model.Account;
import com.casopractico3.CaPr3.model.Client;
import com.casopractico3.CaPr3.model.Movement;
import com.casopractico3.CaPr3.model.OperationType;
import com.casopractico3.CaPr3.repository.AccountRepository;
import com.casopractico3.CaPr3.repository.MovementRepository;
import com.casopractico3.CaPr3.service.OperationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OperationServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MovementRepository movementRepository;

    @InjectMocks
    private OperationService operationService;

    private static final String IBAN = "ES9121000418450200051332";
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(500);

    @Test
    void shouldDepositSuccessfully() {
        Client client = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        client.setId(1L);

        Account account = new Account(client);
        account.setId(1L);
        account.setIban(IBAN);
        account.setBalance(BigDecimal.valueOf(1000));

        Movement movement = Movement.builder()
                .id(1L)
                .account(account)
                .type(OperationType.DEPOSIT)
                .amount(AMOUNT)
                .build();

        when(accountRepository.findByIban(IBAN)).thenReturn(Optional.of(account));
        when(movementRepository.save(any(Movement.class))).thenReturn(movement);

        MovementDTO result = operationService.deposit(IBAN, AMOUNT);

        assertNotNull(result);
        assertEquals("DEPOSIT", result.getType());
        assertEquals(AMOUNT, result.getAmount());
        verify(accountRepository, times(1)).findByIban(IBAN);
        verify(movementRepository, times(1)).save(any(Movement.class));
    }

    @Test
    void shouldThrowExceptionWhenDepositAmountIsZero() {
        assertThrows(IllegalArgumentException.class,
                () -> operationService.deposit(IBAN, BigDecimal.ZERO));
        verify(accountRepository, never()).findByIban(any());
    }

    @Test
    void shouldThrowExceptionWhenDepositAmountIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> operationService.deposit(IBAN, BigDecimal.valueOf(-100)));
        verify(accountRepository, never()).findByIban(any());
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundForDeposit() {
        when(accountRepository.findByIban(IBAN)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> operationService.deposit(IBAN, AMOUNT));
        verify(accountRepository, times(1)).findByIban(IBAN);
    }

    @Test
    void shouldWithdrawSuccessfully() {
        Client client = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        client.setId(1L);

        Account account = new Account(client);
        account.setId(1L);
        account.setIban(IBAN);
        account.setBalance(BigDecimal.valueOf(1000));

        Movement movement = Movement.builder()
                .id(1L)
                .account(account)
                .type(OperationType.WITHDRAW)
                .amount(AMOUNT)
                .build();

        when(accountRepository.findByIban(IBAN)).thenReturn(Optional.of(account));
        when(movementRepository.save(any(Movement.class))).thenReturn(movement);

        MovementDTO result = operationService.withdraw(IBAN, AMOUNT);

        assertNotNull(result);
        assertEquals("WITHDRAW", result.getType());
        verify(accountRepository, times(1)).findByIban(IBAN);
        verify(movementRepository, times(1)).save(any(Movement.class));
    }

    @Test
    void shouldThrowExceptionWhenWithdrawAmountIsZero() {
        assertThrows(IllegalArgumentException.class,
                () -> operationService.withdraw(IBAN, BigDecimal.ZERO));
        verify(accountRepository, never()).findByIban(any());
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundForWithdraw() {
        when(accountRepository.findByIban(IBAN)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> operationService.withdraw(IBAN, AMOUNT));
        verify(accountRepository, times(1)).findByIban(IBAN);
    }

    @Test
    void shouldTransferSuccessfully() {
        Client client1 = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        Client client2 = new Client("Maria", "López", "87654321B", "987654321", "maria@example.com");
        client1.setId(1L);
        client2.setId(2L);

        Account source = new Account(client1);
        source.setId(1L);
        source.setIban("ES9121000418450200051332");
        source.setBalance(BigDecimal.valueOf(1000));

        Account target = new Account(client2);
        target.setId(2L);
        target.setIban("ES9121000418450200051333");
        target.setBalance(BigDecimal.valueOf(500));

        Movement debit = Movement.builder()
                .id(1L)
                .account(source)
                .type(OperationType.OUTGOING_TRANSFER)
                .amount(AMOUNT)
                .build();

        Movement credit = Movement.builder()
                .id(2L)
                .account(target)
                .type(OperationType.INCOMING_TRANSFER)
                .amount(AMOUNT)
                .build();

        when(accountRepository.findByIban(source.getIban())).thenReturn(Optional.of(source));
        when(accountRepository.findByIban(target.getIban())).thenReturn(Optional.of(target));
        when(movementRepository.save(any(Movement.class))).thenReturn(debit).thenReturn(credit);

        List<MovementDTO> result = operationService.transfer(source.getIban(), target.getIban(), AMOUNT);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(accountRepository, times(2)).findByIban(any());
        verify(movementRepository, times(2)).save(any(Movement.class));
    }

    @Test
    void shouldThrowExceptionWhenTransferSameAccount() {
        assertThrows(IllegalArgumentException.class,
                () -> operationService.transfer(IBAN, IBAN, AMOUNT));
        verify(accountRepository, never()).findByIban(any());
    }

    @Test
    void shouldThrowExceptionWhenSourceAccountNotFound() {
        when(accountRepository.findByIban(IBAN)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> operationService.transfer(IBAN, "ES9121000418450200051333", AMOUNT));
        verify(accountRepository, times(1)).findByIban(IBAN);
    }

    @Test
    void shouldThrowExceptionWhenInsufficientBalance() {
        Client client1 = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        Client client2 = new Client("Maria", "López", "87654321B", "987654321", "maria@example.com");
        client1.setId(1L);
        client2.setId(2L);

        Account source = new Account(client1);
        source.setId(1L);
        source.setIban("ES9121000418450200051332");
        source.setBalance(BigDecimal.valueOf(100));

        Account target = new Account(client2);
        target.setId(2L);
        target.setIban("ES9121000418450200051333");
        target.setBalance(BigDecimal.valueOf(500));

        when(accountRepository.findByIban(source.getIban())).thenReturn(Optional.of(source));
        when(accountRepository.findByIban(target.getIban())).thenReturn(Optional.of(target));

        assertThrows(IllegalArgumentException.class,
                () -> operationService.transfer(source.getIban(), target.getIban(), AMOUNT));
    }
}
