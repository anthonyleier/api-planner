package br.com.anthonycruz.planner.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.anthonycruz.planner.models.Trip;

public interface TripRepository extends JpaRepository<Trip, UUID> {
}
