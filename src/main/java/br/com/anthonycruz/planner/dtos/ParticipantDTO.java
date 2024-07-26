package br.com.anthonycruz.planner.dtos;

import java.util.UUID;

public record ParticipantDTO(UUID id, String name, String email, Boolean isConfirmed) {
}
