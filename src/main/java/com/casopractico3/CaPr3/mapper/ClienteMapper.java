package com.casopractico3.CaPr3.mapper;

import com.casopractico3.CaPr3.dto.ClienteDTO;
import com.casopractico3.CaPr3.model.Cliente;

public class ClienteMapper {
    public static ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellidos(),
                cliente.getDni(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getFechaCreacion(),
                cliente.getCuentas().size()
        );
    }

    public static Cliente toEntity(ClienteDTO dto) {
        return new Cliente(
                dto.getNombre(),
                dto.getApellidos(),
                dto.getDni(),
                dto.getTelefono(),
                dto.getEmail()
        );
    }
}