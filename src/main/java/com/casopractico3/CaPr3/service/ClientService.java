package com.casopractico3.CaPr3.service;

import com.casopractico3.CaPr3.dto.ClientDTO;
import com.casopractico3.CaPr3.exception.ClientNotFoundException;
import com.casopractico3.CaPr3.mapper.ClientMapper;
import com.casopractico3.CaPr3.model.Client;
import com.casopractico3.CaPr3.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public List<ClientDTO> listClients() {
        return clientRepository.findAll().stream()
                .map(ClientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClientDTO getsClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client not found: " + id));
        return ClientMapper.toDTO(client);
    }


    public ClientDTO createClient(ClientDTO dto) {
        if (clientRepository.existsByDni(dto.getDni()))
            throw new IllegalArgumentException("A client with that DNI already exists: " +
                    dto.getDni());
        if (clientRepository.existsByEmail(dto.getEmail()))
            throw new IllegalArgumentException("A client with that email already exists: " +
                    dto.getEmail());
        if (clientRepository.existsByPhone(dto.getPhone()))
            throw new IllegalArgumentException("A client with that telephone number already exists: " + dto.getPhone());
                    Client save = clientRepository.save(ClientMapper.toEntity(dto));
        return ClientMapper.toDTO(save);
    }
}