package com.casopractico3.CaPr3.repository;

import com.casopractico3.CaPr3.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    List<Cuenta> findByClienteId(Long clienteId);

    // Carga cuentas con sus movimientos en una sola consulta (evita N+1) 
    @Query("SELECT c FROM Cuenta c LEFT JOIN FETCH c.movimientos WHERE c.cliente.id= :clienteId")
    List<Cuenta> findByClienteIdWithMovimientos(Long clienteId);
} 