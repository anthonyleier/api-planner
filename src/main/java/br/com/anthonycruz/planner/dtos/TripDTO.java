package br.com.anthonycruz.planner.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record TripDTO(
        UUID id,
        String destination,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        boolean isConfirmed,
        String ownerName,
        String ownerEmail) {
}
