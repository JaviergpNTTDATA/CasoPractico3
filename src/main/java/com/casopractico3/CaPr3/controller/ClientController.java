package com.casopractico3.CaPr3.controller;


import com.casopractico3.CaPr3.dto.ClientDTO;
import com.casopractico3.CaPr3.dto.CreateClient;
import com.casopractico3.CaPr3.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Clients", description = "Operaciones relacionadas con clientes")
@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService service;

    @PostMapping
    public ClientDTO create(@Valid @RequestBody CreateClient dto) {
        return service.createClient(dto);
    }

    @Operation(
            summary = "Buscar cliente por id",
            description = "Devuelve un cliente según el id proporcionado"
    )
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    @GetMapping("/obtenerPorId/{id}")
    public ClientDTO searchById(@PathVariable Long id) {
        return service.getsClient(id);
    }


    @Operation(
            summary = "Buscar cliente por DNI",
            description = "Devuelve un cliente según el dni proporcionado"
    )
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    @GetMapping("/obtenerPorDNI/{dni}")
    public ClientDTO searchByDNI(@PathVariable String dni) {
        return service.getClientDNI(dni.toUpperCase());
    }

    @Operation(
            summary = "Listar todos los clientes",
            description = "Devuelve todos los clientes"
    )
    @ApiResponse(responseCode = "200", description = "Clientes encontrado")
    @ApiResponse(responseCode = "404", description = "Clientes no encontrado")
    @GetMapping("/obternerTodos")
    public List<ClientDTO> getAll() {
        return service.listClients();
    }

}