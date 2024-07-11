package br.com.anthonycruz.planner.trip;

import br.com.anthonycruz.planner.exceptions.StartDateAfterEndDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripService {
    @Autowired
    private TripRepository repository;

    public Trip create(TripRequest request){
        Trip trip = new Trip(request);
        if (trip.getStartsAt().isAfter(trip.getEndsAt())) throw new StartDateAfterEndDate();
        this.repository.save(trip);
        return trip;
    }
}
