package com.casopractico3.CaPr3.repositorys;

import com.casopractico3.CaPr3.model.Account;
import com.casopractico3.CaPr3.model.Client;
import com.casopractico3.CaPr3.repository.AccountRepository;
import com.casopractico3.CaPr3.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void shouldSaveAndFindByIban() {

        Client client = clientRepository.save(new Client("John", "Marquez", "7899334R", "65636278", "john@gmail.com"));

        Account account = new Account(client);
        account.setIban("ES11111111111111111111");

        accountRepository.save(account);

        Optional<Account> found =
                accountRepository.findByIban("ES11111111111111111111");

        assertTrue(found.isPresent());
    }

    @Test
    void shouldFindAccountsByClientId() {

        Client client = clientRepository.save(new Client("Anna", "Muñoz", "7865334R", "65666278", "anna@gmail.com"));

        Account account = new Account(client);
        account.setIban("ES11111111111111111111");
        accountRepository.save(account);

        List<Account> accounts = accountRepository.findByClient_Id(client.getId());

        assertEquals(1, accounts.size());
    }

    @Test
    void shouldReturnEmptyWhenIbanNotExists() {

        Optional<Account> account = accountRepository.findByIban("ES999");

        assertTrue(account.isEmpty());
    }
}
