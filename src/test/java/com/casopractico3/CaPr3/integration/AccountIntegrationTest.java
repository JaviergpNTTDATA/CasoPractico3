package com.casopractico3.CaPr3.integration;

import com.casopractico3.CaPr3.model.Client;
import com.casopractico3.CaPr3.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
@Transactional
class AccountIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void shouldReturn403WhenAccountNotExists() throws Exception {

        mockMvc.perform(get("/accounts/ES000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn403WhenClientNotExists() throws Exception {

        mockMvc.perform(get("/accounts/client/999"))
                .andExpect(status().isNotFound());
    }
}
