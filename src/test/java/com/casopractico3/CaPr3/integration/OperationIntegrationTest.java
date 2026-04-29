package com.casopractico3.CaPr3.integration;

import com.casopractico3.CaPr3.model.Account;
import com.casopractico3.CaPr3.model.Client;
import com.casopractico3.CaPr3.model.Movement;
import com.casopractico3.CaPr3.model.OperationType;
import com.casopractico3.CaPr3.repository.AccountRepository;
import com.casopractico3.CaPr3.repository.ClientRepository;
import com.casopractico3.CaPr3.repository.MovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class OperationIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MovementRepository movementRepository;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        movementRepository.deleteAll();
        accountRepository.deleteAll();
        clientRepository.deleteAll();

        Client client = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        client = clientRepository.save(client);

        testAccount = new Account(client);
        testAccount.setIban("ES9121000418450200051332");
        testAccount.setBalance(BigDecimal.valueOf(1000));
        testAccount = accountRepository.save(testAccount);
    }

    @Test
    void shouldDepositPersistCorrectly() {
        Movement movement = new Movement();
        movement.setAccount(testAccount);
        movement.setType(OperationType.DEPOSIT);
        movement.setAmount(BigDecimal.valueOf(500));
        movementRepository.save(movement);

        testAccount.setBalance(testAccount.getBalance().add(BigDecimal.valueOf(500)));
        accountRepository.save(testAccount);

        Account updatedAccount = accountRepository.findByIban(testAccount.getIban()).orElse(null);
        assertNotNull(updatedAccount);
        assertEquals(BigDecimal.valueOf(1500), updatedAccount.getBalance());

        List<Movement> movements = movementRepository.findAll();
        assertEquals(1, movements.size());
        assertEquals(OperationType.DEPOSIT, movements.get(0).getType());
        assertEquals(BigDecimal.valueOf(500), movements.get(0).getAmount());
    }

    @Test
    void shouldWithdrawPersistCorrectly() {
        Movement movement = new Movement();
        movement.setAccount(testAccount);
        movement.setType(OperationType.WITHDRAW);
        movement.setAmount(BigDecimal.valueOf(200));
        movementRepository.save(movement);

        testAccount.setBalance(testAccount.getBalance().subtract(BigDecimal.valueOf(200)));
        accountRepository.save(testAccount);

        Account updatedAccount = accountRepository.findByIban(testAccount.getIban()).orElse(null);
        assertNotNull(updatedAccount);
        assertEquals(BigDecimal.valueOf(800), updatedAccount.getBalance());

        List<Movement> movements = movementRepository.findAll();
        assertEquals(1, movements.size());
        assertEquals(OperationType.WITHDRAW, movements.get(0).getType());
        assertEquals(BigDecimal.valueOf(200), movements.get(0).getAmount());
    }

    @Test
    void shouldHandleMultipleOperationsPersisted() {
        // depósito
        Movement deposit = new Movement();
        deposit.setAccount(testAccount);
        deposit.setType(OperationType.DEPOSIT);
        deposit.setAmount(BigDecimal.valueOf(500));
        movementRepository.save(deposit);

        testAccount.setBalance(testAccount.getBalance().add(BigDecimal.valueOf(500)));
        accountRepository.save(testAccount);

        // retirada
        Movement withdraw = new Movement();
        withdraw.setAccount(testAccount);
        withdraw.setType(OperationType.WITHDRAW);
        withdraw.setAmount(BigDecimal.valueOf(200));
        movementRepository.save(withdraw);

        testAccount.setBalance(testAccount.getBalance().subtract(BigDecimal.valueOf(200)));
        accountRepository.save(testAccount);

        Account updatedAccount = accountRepository.findByIban(testAccount.getIban()).orElse(null);
        assertNotNull(updatedAccount);
        assertEquals(BigDecimal.valueOf(1300), updatedAccount.getBalance());

        List<Movement> movements = movementRepository.findAll();
        assertEquals(2, movements.size());
    }
}
