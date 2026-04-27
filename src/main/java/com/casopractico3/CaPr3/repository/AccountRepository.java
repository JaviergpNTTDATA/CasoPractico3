package com.casopractico3.CaPr3.repository;

import com.casopractico3.CaPr3.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByIban(String iban);
    List<Account> findByClient_Id(Long clientId);

    // Carga cuentas con sus movimientos en una sola consulta (evita N+1) 
    @Query("""
       SELECT a
       FROM Account a
       LEFT JOIN FETCH a.movements
       WHERE a.client.id = :clientId
       """)
    List<Account> findByClienteIdWithMovimientos(Long clienteId);
}