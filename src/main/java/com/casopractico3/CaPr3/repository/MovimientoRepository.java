package com.casopractico3.CaPr3.repository;

import com.casopractico3.CaPr3.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaIdOrderByFechaDesc(Long cuentaId);
    @Query("SELECT m FROM movimientos m WHERE cuenta_id = ? AND fecha BETWEEN ? AND ? ")
    List<Movimiento> findByCuentaIdAndFechaBetweenOrderByFechaDesc(Long cuentaId, LocalDateTime inicio, LocalDateTime fin);
} 