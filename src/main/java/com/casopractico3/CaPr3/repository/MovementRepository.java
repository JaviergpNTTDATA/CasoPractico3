package com.casopractico3.CaPr3.repository;

import com.casopractico3.CaPr3.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    List<Movement> findByAccount_ibanOrderByCreatedAtDesc(String iban);

    List<Movement> findByAccount_ibanAndCreatedAtBetweenOrderByCreatedAtDesc(String iban, LocalDateTime start, LocalDateTime end
    );

}