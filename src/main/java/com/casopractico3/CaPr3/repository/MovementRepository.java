package com.casopractico3.CaPr3.repository;

import com.casopractico3.CaPr3.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    List<Movement> findByCuentaIdOrderByFechaDesc(Long cuentaId);
    @Query("SELECT m FROM movimientos m WHERE cuenta_id = ? AND fecha BETWEEN ? AND ? ")
    List<Movement> findByCuentaIdAndFechaBetweenOrderByFechaDesc(Long cuentaId, LocalDateTime inicio, LocalDateTime fin);
} 