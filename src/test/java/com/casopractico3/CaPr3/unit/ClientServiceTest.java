package com.casopractico3.CaPr3.unit;

import com.casopractico3.CaPr3.dto.ClientDTO;
import com.casopractico3.CaPr3.dto.CreateClient;
import com.casopractico3.CaPr3.exception.ClientNotFoundException;
import com.casopractico3.CaPr3.mapper.ClientMapper;
import com.casopractico3.CaPr3.model.Client;
import com.casopractico3.CaPr3.repository.ClientRepository;
import com.casopractico3.CaPr3.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void shouldCreateClientSuccessfully() {
        CreateClient dto = new CreateClient();
        dto.setFirstName("Juan");
        dto.setLastName("García");
        dto.setDni("12345678A");
        dto.setEmail("juan@example.com");
        dto.setPhone("123456789");

        Client client = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        client.setId(1L);

        when(clientRepository.existsByDni(dto.getDni())).thenReturn(false);
        when(clientRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(clientRepository.existsByPhone(dto.getPhone())).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        ClientDTO result = clientService.createClient(dto);

        assertNotNull(result);
        assertEquals("Juan", result.getFirstName());
        assertEquals("García", result.getLastName());
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    void shouldThrowExceptionWhenClientDniAlreadyExists() {
        CreateClient dto = new CreateClient();
        dto.setFirstName("Juan");
        dto.setLastName("García");
        dto.setDni("12345678A");
        dto.setEmail("juan@example.com");
        dto.setPhone("123456789");

        when(clientRepository.existsByDni(dto.getDni())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> clientService.createClient(dto));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenClientEmailAlreadyExists() {
        CreateClient dto = new CreateClient();
        dto.setFirstName("Juan");
        dto.setLastName("García");
        dto.setDni("12345678A");
        dto.setEmail("juan@example.com");
        dto.setPhone("123456789");

        when(clientRepository.existsByDni(dto.getDni())).thenReturn(false);
        when(clientRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> clientService.createClient(dto));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenClientPhoneAlreadyExists() {
        CreateClient dto = new CreateClient();
        dto.setFirstName("Juan");
        dto.setLastName("García");
        dto.setDni("12345678A");
        dto.setEmail("juan@example.com");
        dto.setPhone("123456789");

        when(clientRepository.existsByDni(dto.getDni())).thenReturn(false);
        when(clientRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(clientRepository.existsByPhone(dto.getPhone())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> clientService.createClient(dto));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenFirstNameIsBlank() {
        CreateClient dto = new CreateClient();
        dto.setFirstName("");
        dto.setLastName("García");
        dto.setDni("12345678A");
        dto.setEmail("juan@example.com");
        dto.setPhone("123456789");

        assertThrows(IllegalArgumentException.class, () -> clientService.createClient(dto));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldListAllClientsSuccessfully() {
        Client client1 = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        Client client2 = new Client("Maria", "López", "87654321B", "987654321", "maria@example.com");
        client1.setId(1L);
        client2.setId(2L);

        when(clientRepository.findAll()).thenReturn(Arrays.asList(client1, client2));

        List<ClientDTO> result = clientService.listClients();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void shouldGetClientByIdSuccessfully() {
        Client client = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        client.setId(1L);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        ClientDTO result = clientService.getsClient(1L);

        assertNotNull(result);
        assertEquals("Juan", result.getFirstName());
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenClientNotFoundById() {
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.getsClient(999L));
        verify(clientRepository, times(1)).findById(999L);
    }

    @Test
    void shouldGetClientByDniSuccessfully() {
        Client client = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        client.setId(1L);

        when(clientRepository.findByDni("12345678A")).thenReturn(Optional.of(client));

        ClientDTO result = clientService.getClientDNI("12345678A");

        assertNotNull(result);
        assertEquals("12345678A", result.getDni());
        verify(clientRepository, times(1)).findByDni("12345678A");
    }

    @Test
    void shouldThrowExceptionWhenClientNotFoundByDni() {
        when(clientRepository.findByDni("99999999Z")).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> clientService.getClientDNI("99999999Z"));
        verify(clientRepository, times(1)).findByDni("99999999Z");
    }
}
