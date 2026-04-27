package com.casopractico3.CaPr3.repository;

import com.casopractico3.CaPr3.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByNumeroCuenta(String numeroCuenta);
    List<Account> findByClienteId(Long clienteId);

    // Carga cuentas con sus movimientos en una sola consulta (evita N+1) 
    @Query("SELECT c FROM Cuenta c LEFT JOIN FETCH c.movimientos WHERE c.cliente.id= :clienteId")
    List<Account> findByClienteIdWithMovimientos(Long clienteId);
} 