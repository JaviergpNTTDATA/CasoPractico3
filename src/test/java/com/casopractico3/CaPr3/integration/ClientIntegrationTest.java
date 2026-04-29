package com.casopractico3.CaPr3.integration;

import com.casopractico3.CaPr3.dto.CreateClient;
import com.casopractico3.CaPr3.model.Client;
import com.casopractico3.CaPr3.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ClientIntegrationTest {

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();
    }

    @Test
    void shouldCreateClientPersisted() {
        Client client = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        clientRepository.save(client);

        Client persisted = clientRepository.findByDni("12345678A").orElse(null);
        assertNotNull(persisted);
        assertEquals("Juan", persisted.getFirstName());
        assertEquals("juan@example.com", persisted.getEmail());
    }

    @Test
    void shouldGetClientById() {
        Client client = new Client("Maria", "López", "87654321B", "987654321", "maria@example.com");
        Client saved = clientRepository.save(client);

        Client found = clientRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Maria", found.getFirstName());
    }

    @Test
    void shouldReturnEmptyWhenIdDoesNotExist() {
        assertTrue(clientRepository.findById(999L).isEmpty());
    }

    @Test
    void shouldGetClientByDni() {
        Client client = new Client("Carlos", "Ruiz", "11111111C", "111111111", "carlos@example.com");
        clientRepository.save(client);

        Client found = clientRepository.findByDni("11111111C").orElse(null);
        assertNotNull(found);
        assertEquals("Carlos", found.getFirstName());
    }

    @Test
    void shouldReturnEmptyWhenDniDoesNotExist() {
        assertTrue(clientRepository.findByDni("99999999Z").isEmpty());
    }

    @Test
    void shouldListAllClients() {
        Client client1 = new Client("Ana", "Martín", "22222222D", "222222222", "ana@example.com");
        Client client2 = new Client("Pedro", "Gómez", "33333333E", "333333333", "pedro@example.com");

        clientRepository.save(client1);
        clientRepository.save(client2);

        List<Client> clients = clientRepository.findAll();
        assertTrue(clients.size() >= 2);
    }

    @Test
    void shouldListEmptyWhenNoClients() {
        List<Client> clients = clientRepository.findAll();
        assertEquals(0, clients.size());
    }

    @Test
    void shouldRejectDuplicateDniByConstraint() {
        Client client1 = new Client("Laura", "Fernández", "44444444F", "444444444", "laura@example.com");
        clientRepository.save(client1);

        Client client2 = new Client("David", "Sánchez", "44444444F", "555555555", "david@example.com");

        assertThrows(Exception.class, () -> clientRepository.saveAndFlush(client2));
    }

    @Test
    void shouldPersistClientDataSuccessfully() {
        CreateClient createClient = new CreateClient();
        createClient.setFirstName("Gracia");
        createClient.setLastName("García");
        createClient.setDni("99999999K");
        createClient.setEmail("gracia@example.com");
        createClient.setPhone("999999999");

        Client client = new Client(
                createClient.getFirstName(),
                createClient.getLastName(),
                createClient.getDni(),
                createClient.getPhone(),
                createClient.getEmail()
        );
        clientRepository.save(client);

        Client persistedClient = clientRepository.findByDni("99999999K").orElse(null);

        assertNotNull(persistedClient);
        assertEquals("Gracia", persistedClient.getFirstName());
        assertEquals("gracia@example.com", persistedClient.getEmail());
    }
}
