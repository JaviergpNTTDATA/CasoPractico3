package com.casopractico3.CaPr3.repository;

import com.casopractico3.CaPr3.model.Account;
import com.casopractico3.CaPr3.model.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldSaveAccountSuccessfully() {
        Client client = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        Client savedClient = clientRepository.save(client);
        entityManager.flush();

        Account account = new Account(savedClient);
        account.setIban("ES9121000418450200051332");
        account.setBalance(BigDecimal.valueOf(1000));

        Account saved = accountRepository.save(account);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("ES9121000418450200051332", saved.getIban());
        assertEquals(BigDecimal.valueOf(1000), saved.getBalance());
    }

    @Test
    void shouldFindAccountByIdSuccessfully() {
        Client client = new Client("Maria", "López", "87654321B", "987654321", "maria@example.com");
        Client savedClient = clientRepository.save(client);
        entityManager.flush();

        Account account = new Account(savedClient);
        account.setIban("ES9121000418450200051333");
        account.setBalance(BigDecimal.valueOf(2000));
        Account saved = accountRepository.save(account);
        entityManager.flush();

        Optional<Account> found = accountRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("ES9121000418450200051333", found.get().getIban());
    }

    @Test
    void shouldReturnEmptyWhenAccountNotFound() {
        Optional<Account> found = accountRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void shouldFindAllAccountsSuccessfully() {
        Client client = new Client("Carlos", "Ruiz", "11111111C", "111111111", "carlos@example.com");
        Client savedClient = clientRepository.save(client);
        entityManager.flush();

        Account account1 = new Account(savedClient);
        account1.setIban("ES9121000418450200051334");
        account1.setBalance(BigDecimal.valueOf(1500));

        Account account2 = new Account(savedClient);
        account2.setIban("ES9121000418450200051335");
        account2.setBalance(BigDecimal.valueOf(2500));

        accountRepository.save(account1);
        accountRepository.save(account2);
        entityManager.flush();

        List<Account> all = accountRepository.findAll();

        assertNotNull(all);
        assertTrue(all.size() >= 2);
    }

    @Test
    void shouldDeleteAccountSuccessfully() {
        Client client = new Client("Ana", "Martín", "22222222D", "222222222", "ana@example.com");
        Client savedClient = clientRepository.save(client);
        entityManager.flush();

        Account account = new Account(savedClient);
        account.setIban("ES9121000418450200051336");
        account.setBalance(BigDecimal.valueOf(3000));
        Account saved = accountRepository.save(account);
        entityManager.flush();

        accountRepository.deleteById(saved.getId());
        entityManager.flush();

        Optional<Account> found = accountRepository.findById(saved.getId());

        assertFalse(found.isPresent());
    }

    @Test
    void shouldFindAccountByIbanSuccessfully() {
        Client client = new Client("Pedro", "Gómez", "33333333E", "333333333", "pedro@example.com");
        Client savedClient = clientRepository.save(client);
        entityManager.flush();

        Account account = new Account(savedClient);
        account.setIban("ES9121000418450200051337");
        account.setBalance(BigDecimal.valueOf(4000));
        accountRepository.save(account);
        entityManager.flush();

        Optional<Account> found = accountRepository.findByIban("ES9121000418450200051337");

        assertTrue(found.isPresent());
        assertEquals(BigDecimal.valueOf(4000), found.get().getBalance());
    }

    @Test
    void shouldReturnEmptyWhenAccountNotFoundByIban() {
        Optional<Account> found = accountRepository.findByIban("INVALID_IBAN");

        assertFalse(found.isPresent());
    }

    @Test
    void shouldFindAccountsByClientIdSuccessfully() {
        Client client = new Client("Laura", "Fernández", "44444444F", "444444444", "laura@example.com");
        Client savedClient = clientRepository.save(client);
        entityManager.flush();

        Account account1 = new Account(savedClient);
        account1.setIban("ES9121000418450200051338");
        account1.setBalance(BigDecimal.valueOf(5000));

        Account account2 = new Account(savedClient);
        account2.setIban("ES9121000418450200051339");
        account2.setBalance(BigDecimal.valueOf(6000));

        accountRepository.save(account1);
        accountRepository.save(account2);
        entityManager.flush();

        List<Account> found = accountRepository.findByClient_Id(savedClient.getId());

        assertNotNull(found);
        assertEquals(2, found.size());
    }

    @Test
    void shouldReturnEmptyListWhenClientHasNoAccounts() {
        List<Account> found = accountRepository.findByClient_Id(999L);

        assertNotNull(found);
        assertTrue(found.isEmpty());
    }

    @Test
    void shouldUpdateAccountSuccessfully() {
        Client client = new Client("David", "Sánchez", "55555555G", "555555555", "david@example.com");
        Client savedClient = clientRepository.save(client);
        entityManager.flush();

        Account account = new Account(savedClient);
        account.setIban("ES9121000418450200051340");
        account.setBalance(BigDecimal.valueOf(1000));
        Account saved = accountRepository.save(account);
        entityManager.flush();

        saved.setBalance(BigDecimal.valueOf(1500));
        accountRepository.save(saved);
        entityManager.flush();

        Optional<Account> updated = accountRepository.findById(saved.getId());

        assertTrue(updated.isPresent());
        assertEquals(BigDecimal.valueOf(1500), updated.get().getBalance());
    }

    @Test
    void shouldHandleAccountWithNullBalance() {
        Client client = new Client("Sofia", "Torres", "66666666H", "666666666", "sofia@example.com");
        Client savedClient = clientRepository.save(client);
        entityManager.flush();

        Account account = new Account(savedClient);
        account.setIban("ES9121000418450200051341");

        Account saved = accountRepository.save(account);
        entityManager.flush();

        Optional<Account> found = accountRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertNotNull(found.get().getBalance());
        assertEquals(BigDecimal.ZERO, found.get().getBalance());
    }
}
