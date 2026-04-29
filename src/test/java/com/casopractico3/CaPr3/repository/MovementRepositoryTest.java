package com.casopractico3.CaPr3.repository;

import com.casopractico3.CaPr3.model.Account;
import com.casopractico3.CaPr3.model.Client;
import com.casopractico3.CaPr3.model.Movement;
import com.casopractico3.CaPr3.model.OperationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MovementRepositoryTest {

    @Autowired
    private MovementRepository movementRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    void shouldReturnEmptyWhenMovementNotFound() {
        Optional<Movement> found = movementRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    
}
