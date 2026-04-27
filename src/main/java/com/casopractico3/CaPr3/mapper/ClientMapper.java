package com.casopractico3.CaPr3.mapper;

import com.casopractico3.CaPr3.dto.ClientDTO;
import com.casopractico3.CaPr3.model.Client;

public class ClientMapper {
    public static ClientDTO toDTO(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getNombre(),
                client.getApellidos(),
                client.getDni(),
                client.getEmail(),
                client.getTelefono(),
                client.getFechaCreacion(),
                client.getCuentas().size()
        );
    }

    public static Client toEntity(ClientDTO dto) {
        return new Client(
                dto.getNombre(),
                dto.getApellidos(),
                dto.getDni(),
                dto.getTelefono(),
                dto.getEmail()
        );
    }
}