package com.casopractico3.CaPr3.repository;

import com.casopractico3.CaPr3.model.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldSaveClientSuccessfully() {
        Client client = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");

        Client saved = clientRepository.save(client);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("Juan", saved.getFirstName());
        assertEquals("12345678A", saved.getDni());
    }

    @Test
    void shouldFindClientByIdSuccessfully() {
        Client client = new Client("Maria", "López", "87654321B", "987654321", "maria@example.com");
        Client saved = clientRepository.save(client);
        entityManager.flush();

        Optional<Client> found = clientRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Maria", found.get().getFirstName());
    }

    @Test
    void shouldReturnEmptyWhenClientNotFound() {
        Optional<Client> found = clientRepository.findById(999L);

        assertFalse(found.isPresent());
    }

    @Test
    void shouldFindAllClientsSuccessfully() {
        Client client1 = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        Client client2 = new Client("Maria", "López", "87654321B", "987654321", "maria@example.com");

        clientRepository.save(client1);
        clientRepository.save(client2);
        entityManager.flush();

        List<Client> all = clientRepository.findAll();

        assertNotNull(all);
        assertTrue(all.size() >= 2);
    }

    @Test
    void shouldDeleteClientSuccessfully() {
        Client client = new Client("Carlos", "Ruiz", "11111111C", "111111111", "carlos@example.com");
        Client saved = clientRepository.save(client);
        entityManager.flush();

        clientRepository.deleteById(saved.getId());
        entityManager.flush();

        Optional<Client> found = clientRepository.findById(saved.getId());

        assertFalse(found.isPresent());
    }

    @Test
    void shouldFindClientByDniSuccessfully() {
        Client client = new Client("Ana", "Martín", "22222222D", "222222222", "ana@example.com");
        clientRepository.save(client);
        entityManager.flush();

        Optional<Client> found = clientRepository.findByDni("22222222D");

        assertTrue(found.isPresent());
        assertEquals("Ana", found.get().getFirstName());
    }

    @Test
    void shouldReturnEmptyWhenClientNotFoundByDni() {
        Optional<Client> found = clientRepository.findByDni("99999999Z");

        assertFalse(found.isPresent());
    }

    @Test
    void shouldReturnTrueWhenClientExistsByDni() {
        Client client = new Client("Pedro", "Gómez", "33333333E", "333333333", "pedro@example.com");
        clientRepository.save(client);
        entityManager.flush();

        boolean exists = clientRepository.existsByDni("33333333E");

        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenClientNotExistsByDni() {
        boolean exists = clientRepository.existsByDni("88888888X");

        assertFalse(exists);
    }

    @Test
    void shouldReturnTrueWhenClientExistsByEmail() {
        Client client = new Client("Laura", "Fernández", "44444444F", "444444444", "laura@example.com");
        clientRepository.save(client);
        entityManager.flush();

        boolean exists = clientRepository.existsByEmail("laura@example.com");

        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenClientNotExistsByEmail() {
        boolean exists = clientRepository.existsByEmail("nonexistent@example.com");

        assertFalse(exists);
    }

    @Test
    void shouldReturnTrueWhenClientExistsByPhone() {
        Client client = new Client("David", "Sánchez", "55555555G", "555555555", "david@example.com");
        clientRepository.save(client);
        entityManager.flush();

        boolean exists = clientRepository.existsByPhone("555555555");

        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenClientNotExistsByPhone() {
        boolean exists = clientRepository.existsByPhone("999999999");

        assertFalse(exists);
    }

    @Test
    void shouldUpdateClientSuccessfully() {
        Client client = new Client("Sofia", "Torres", "66666666H", "666666666", "sofia@example.com");
        Client saved = clientRepository.save(client);
        entityManager.flush();

        saved.setFirstName("Sofía");
        saved.setPhone("777777777");
        clientRepository.save(saved);
        entityManager.flush();

        Optional<Client> updated = clientRepository.findById(saved.getId());

        assertTrue(updated.isPresent());
        assertEquals("Sofía", updated.get().getFirstName());
        assertEquals("777777777", updated.get().getPhone());
    }
}
