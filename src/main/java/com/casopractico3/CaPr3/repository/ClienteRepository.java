package com.casopractico3.CaPr3.repository;

import com.casopractico3.CaPr3.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repositoºry
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByDni(String dni);
    Optional<Cliente> findByEmail(String email);
    boolean existsByDni(String dni);
    boolean existsByEmail(String email);
    boolean existsByTelefono(String telefono);
} 