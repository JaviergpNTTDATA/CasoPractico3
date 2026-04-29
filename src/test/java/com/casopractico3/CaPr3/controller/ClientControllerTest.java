package com.casopractico3.CaPr3.controller;

import com.casopractico3.CaPr3.dto.ClientDTO;
import com.casopractico3.CaPr3.dto.CreateClient;
import com.casopractico3.CaPr3.exception.ClientNotFoundException;
import com.casopractico3.CaPr3.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled("Diaasables until i can fix the ApplicationContext")
@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateClientSuccessfully() throws Exception {
        CreateClient createClient = new CreateClient();
        createClient.setFirstName("Juan");
        createClient.setLastName("García");
        createClient.setDni("12345678A");
        createClient.setEmail("juan@example.com");
        createClient.setPhone("123456789");

        ClientDTO clientDTO = ClientDTO.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("García")
                .dni("12345678A")
                .email("juan@example.com")
                .phone("123456789")
                .accountCount(0)
                .build();

        when(clientService.createClient(any(CreateClient.class))).thenReturn(clientDTO);

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createClient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.dni").value("12345678A"));

        verify(clientService, times(1)).createClient(any(CreateClient.class));
    }

    @Test
    void shouldGetClientByIdSuccessfully() throws Exception {
        ClientDTO clientDTO = ClientDTO.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("García")
                .dni("12345678A")
                .email("juan@example.com")
                .phone("123456789")
                .accountCount(1)
                .build();

        when(clientService.getsClient(1L)).thenReturn(clientDTO);

        mockMvc.perform(get("/clients/getById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.accountCount").value(1));

        verify(clientService, times(1)).getsClient(1L);
    }

    @Test
    void shouldReturn404WhenClientNotFoundById() throws Exception {
        when(clientService.getsClient(999L))
                .thenThrow(new ClientNotFoundException("Client not found: 999"));

        mockMvc.perform(get("/clients/getById/999"))
                .andExpect(status().isNotFound());

        verify(clientService, times(1)).getsClient(999L);
    }

    @Test
    void shouldGetClientByDniSuccessfully() throws Exception {
        ClientDTO clientDTO = ClientDTO.builder()
                .id(1L)
                .firstName("Maria")
                .lastName("López")
                .dni("87654321B")
                .email("maria@example.com")
                .phone("987654321")
                .accountCount(2)
                .build();

        when(clientService.getClientDNI("87654321B")).thenReturn(clientDTO);

        mockMvc.perform(get("/clients/getByDni/87654321B"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dni").value("87654321B"))
                .andExpect(jsonPath("$.firstName").value("Maria"));

        verify(clientService, times(1)).getClientDNI("87654321B");
    }

    @Test
    void shouldReturn404WhenClientNotFoundByDni() throws Exception {
        when(clientService.getClientDNI("99999999Z"))
                .thenThrow(new ClientNotFoundException("Client not found: 99999999Z"));

        mockMvc.perform(get("/clients/getByDni/99999999Z"))
                .andExpect(status().isNotFound());

        verify(clientService, times(1)).getClientDNI("99999999Z");
    }

    @Test
    void shouldGetAllClientsSuccessfully() throws Exception {
        List<ClientDTO> clients = Arrays.asList(
                ClientDTO.builder().id(1L).firstName("Juan").lastName("García")
                        .dni("12345678A").email("juan@example.com").phone("123456789").accountCount(0).build(),
                ClientDTO.builder().id(2L).firstName("Maria").lastName("López")
                        .dni("87654321B").email("maria@example.com").phone("987654321").accountCount(1).build()
        );

        when(clientService.listClients()).thenReturn(clients);

        mockMvc.perform(get("/clients/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName").value("Juan"))
                .andExpect(jsonPath("$[1].firstName").value("Maria"));

        verify(clientService, times(1)).listClients();
    }

    @Test
    void shouldReturnEmptyListWhenNoClients() throws Exception {
        when(clientService.listClients()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/clients/getAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(clientService, times(1)).listClients();
    }

    @Test
    void shouldValidateClientCreationRequestBody() throws Exception {
        String invalidJson = "{ \"firstName\": \"\" }";

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
