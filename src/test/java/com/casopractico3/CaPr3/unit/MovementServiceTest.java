package com.casopractico3.CaPr3.unit;

import com.casopractico3.CaPr3.model.Account;
import com.casopractico3.CaPr3.model.Client;
import com.casopractico3.CaPr3.model.Movement;
import com.casopractico3.CaPr3.model.OperationType;
import com.casopractico3.CaPr3.repository.MovementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovementServiceTest {

    @Mock
    private MovementRepository movementRepository;

    @Test
    void shouldSaveMovementSuccessfully() {
        Client client = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        client.setId(1L);

        Account account = new Account(client);
        account.setId(1L);
        account.setIban("ES9121000418450200051332");
        account.setBalance(BigDecimal.valueOf(1000));

        Movement movement = Movement.builder()
                .id(1L)
                .account(account)
                .type(OperationType.DEPOSIT)
                .amount(BigDecimal.valueOf(500))
                .createdAt(LocalDateTime.now())
                .build();

        when(movementRepository.save(any(Movement.class))).thenReturn(movement);

        Movement result = movementRepository.save(movement);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(OperationType.DEPOSIT, result.getType());
        verify(movementRepository, times(1)).save(any(Movement.class));
    }

    @Test
    void shouldFindMovementByIdSuccessfully() {
        Client client = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        client.setId(1L);

        Account account = new Account(client);
        account.setId(1L);
        account.setIban("ES9121000418450200051332");

        Movement movement = Movement.builder()
                .id(1L)
                .account(account)
                .type(OperationType.WITHDRAW)
                .amount(BigDecimal.valueOf(200))
                .build();

        when(movementRepository.findById(1L)).thenReturn(Optional.of(movement));

        Optional<Movement> result = movementRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(OperationType.WITHDRAW, result.get().getType());
        verify(movementRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnEmptyWhenMovementNotFound() {
        when(movementRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Movement> result = movementRepository.findById(999L);

        assertFalse(result.isPresent());
        verify(movementRepository, times(1)).findById(999L);
    }

    @Test
    void shouldFindAllMovementsSuccessfully() {
        Client client = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        client.setId(1L);

        Account account = new Account(client);
        account.setId(1L);
        account.setIban("ES9121000418450200051332");

        Movement movement1 = Movement.builder()
                .id(1L)
                .account(account)
                .type(OperationType.DEPOSIT)
                .amount(BigDecimal.valueOf(500))
                .build();

        Movement movement2 = Movement.builder()
                .id(2L)
                .account(account)
                .type(OperationType.WITHDRAW)
                .amount(BigDecimal.valueOf(200))
                .build();

        when(movementRepository.findAll()).thenReturn(Arrays.asList(movement1, movement2));

        List<Movement> result = movementRepository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(movementRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteMovementSuccessfully() {
        Long movementId = 1L;

        movementRepository.deleteById(movementId);

        verify(movementRepository, times(1)).deleteById(movementId);
    }
}
