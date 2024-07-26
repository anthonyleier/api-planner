package br.com.anthonycruz.planner.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.anthonycruz.planner.exceptions.StartDateAfterEndDate;
import br.com.anthonycruz.planner.models.Trip;
import br.com.anthonycruz.planner.repositories.TripRepository;
import br.com.anthonycruz.planner.requests.TripRequest;

@Service
public class TripService {
    private final TripRepository repository;

    public TripService(TripRepository repository) {
        this.repository = repository;
    }

    public Trip create(TripRequest request) {
        Trip trip = new Trip(request);
        if (trip.getStartsAt().isAfter(trip.getEndsAt())) throw new StartDateAfterEndDate();
        return this.repository.save(trip);
    }

    public Trip update(Trip trip, String destination, LocalDateTime startsAt, LocalDateTime endsAt) {
        trip.setDestination(destination);
        trip.setStartsAt(startsAt);
        trip.setEndsAt(endsAt);
        return this.repository.save(trip);
    }

    public Optional<Trip> findById(UUID id) {
        return this.repository.findById(id);
    }

    public Trip confirm(Trip trip) {
        trip.setConfirmed(true);
        return this.repository.save(trip);
    }
}
