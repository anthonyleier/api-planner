package br.com.anthonycruz.planner.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.anthonycruz.planner.models.Link;

public interface LinkRepository extends JpaRepository<Link, UUID> {
    List<Link> findByTripId(UUID id);
}
