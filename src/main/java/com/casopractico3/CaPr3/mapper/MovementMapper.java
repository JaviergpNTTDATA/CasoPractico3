package com.casopractico3.CaPr3.mapper;

import com.casopractico3.CaPr3.dto.MovementDTO;
import com.casopractico3.CaPr3.model.Movement;
import com.casopractico3.CaPr3.model.OperationType;
import org.springframework.stereotype.Component;

@Component
public class MovementMapper {

    public static MovementDTO toDto(Movement movement) {
        MovementDTO dto = new MovementDTO();
        dto.setIban(movement.getAccount().getIban());
        dto.setAmount(movement.getAmount());
        dto.setType(movement.getType().name());
        dto.setCreatedAt(movement.getCreatedAt());
        return dto;
    }

    public MovementDTO toDtoN(Movement movement) {
        MovementDTO dto = new MovementDTO();
        dto.setIban(movement.getAccount().getIban());
        dto.setAmount(movement.getAmount());
        dto.setType(movement.getType().name());
        dto.setCreatedAt(movement.getCreatedAt());
        return dto;
    }

    public Movement toEntity(MovementDTO dto) {
        Movement movement = new Movement();
        movement.setAmount(dto.getAmount());
        movement.setType(OperationType.valueOf(dto.getType().toUpperCase()));
        movement.setCreatedAt(dto.getCreatedAt());
        return movement;
    }
}
