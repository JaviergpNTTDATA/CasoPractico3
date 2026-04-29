package com.casopractico3.CaPr3.controllers;

import com.casopractico3.CaPr3.controller.ClientController;
import com.casopractico3.CaPr3.exception.ClientNotFoundException;
import com.casopractico3.CaPr3.security.JwtFilter;
import com.casopractico3.CaPr3.service.ClientService;
import com.casopractico3.CaPr3.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Test
    void shouldReturnClientById() throws Exception {
        // Ajusta la URL si tu ClientController usa otro path
        mockMvc.perform(get("/clients/getById/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenClientNotFound() throws Exception {

        when(clientService.getsClient(99L))
                .thenThrow(new ClientNotFoundException("Client not found"));

        mockMvc.perform(get("/clients/99"))
                .andExpect(status().isNotFound());
    }
}
