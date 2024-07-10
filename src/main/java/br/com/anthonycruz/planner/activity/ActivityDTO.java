package br.com.anthonycruz.planner.activity;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityDTO(UUID id, String title, LocalDateTime occursAt) {
}
