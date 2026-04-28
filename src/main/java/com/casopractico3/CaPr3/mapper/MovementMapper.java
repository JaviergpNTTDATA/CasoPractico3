package com.casopractico3.CaPr3.mapper;

import com.casopractico3.CaPr3.dto.MovementDTO;
import com.casopractico3.CaPr3.model.Movement;

public class MovementMapper {

    public static MovementDTO toDTO(Movement movement) {
        return new MovementDTO(
                movement.getAccount().getIban(),
                movement.getType().name(),
                movement.getAmount(),
                movement.getCreatedAt()
        );
    }
}
