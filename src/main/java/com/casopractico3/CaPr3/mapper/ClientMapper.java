package com.casopractico3.CaPr3.mapper;

import com.casopractico3.CaPr3.dto.ClientDTO;
import com.casopractico3.CaPr3.model.Client;
import lombok.Builder;

public class ClientMapper {
    public static ClientDTO toDTO(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getDni(),
                client.getEmail(),
                client.getPhone(),
                client.getCreatedAt(),
                client.getAccounts().size()
        );
    }

    public static Client toEntity(ClientDTO dto) {
        return new Client(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getDni(),
                dto.getPhone(),
                dto.getEmail()
        );
    }
}