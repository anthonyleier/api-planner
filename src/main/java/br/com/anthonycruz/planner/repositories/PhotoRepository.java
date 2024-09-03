package br.com.anthonycruz.planner.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.anthonycruz.planner.models.Photo;

public interface PhotoRepository extends JpaRepository<Photo, UUID> {
    List<Photo> findByTripId(UUID id);
}
