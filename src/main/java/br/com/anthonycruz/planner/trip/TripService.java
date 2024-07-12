package br.com.anthonycruz.planner.trip;

import br.com.anthonycruz.planner.exceptions.StartDateAfterEndDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TripService {
    @Autowired
    private TripRepository repository;

    public Trip create(TripRequest request) {
        Trip trip = new Trip(request);
        if (trip.getStartsAt().isAfter(trip.getEndsAt())) throw new StartDateAfterEndDate();
        this.repository.save(trip);
        return trip;
    }

    public void update(Trip trip, String destination, LocalDateTime startsAt, LocalDateTime endsAt) {
        trip.setDestination(destination);
        trip.setStartsAt(startsAt);
        trip.setEndsAt(endsAt);
        this.repository.save(trip);
    }

    public Optional<Trip> findById(UUID id) {
        return this.repository.findById(id);
    }

    public void confirm(Trip trip) {
        trip.setConfirmed(true);
        this.repository.save(trip);
    }
}
