package com.casopractico3.CaPr3.repository;

import com.casopractico3.CaPr3.model.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    List<Movement> findByAccount_IdOrderByCreatedAtDesc(Long accountId);

    List<Movement> findByAccount_IdAndCreatedAtBetweenOrderByCreatedAtDesc(
            Long accountId,
            LocalDateTime start,
            LocalDateTime end
    );
} 