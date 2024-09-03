package br.com.anthonycruz.planner.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.anthonycruz.planner.models.Photo;
import br.com.anthonycruz.planner.models.Trip;

public interface PhotoRepository extends JpaRepository<Photo, UUID> {
    List<Photo> findByTripId(UUID id);

    boolean existsByFilename(String originalFilename);

    Optional<Photo> findByFilenameAndTrip(String filename, Trip trip);
}
