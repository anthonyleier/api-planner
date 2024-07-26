package br.com.anthonycruz.planner.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityDTO(UUID id, String title, LocalDateTime occursAt) {
}
