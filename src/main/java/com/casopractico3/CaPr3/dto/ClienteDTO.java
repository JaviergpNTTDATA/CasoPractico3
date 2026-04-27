package com.casopractico3.CaPr3.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
public class ClienteDTO {
    private Long id;
    private String nombre;
    private String apellidos;   // mantiene el mismo nombre que el modelo

    private String dni;
    private String email;
    private String telefono;
    private LocalDateTime fechaCreacion;
    private int numeroCuentas;  // campo calculado
    // Constructores, getters y setters...
}