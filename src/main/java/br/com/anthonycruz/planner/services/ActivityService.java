package br.com.anthonycruz.planner.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.anthonycruz.planner.dtos.ActivityDTO;
import br.com.anthonycruz.planner.exceptions.ActivityDateNotInTripRange;
import br.com.anthonycruz.planner.models.Activity;
import br.com.anthonycruz.planner.models.Trip;
import br.com.anthonycruz.planner.repositories.ActivityRepository;
import br.com.anthonycruz.planner.requests.ActivityRequest;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository repository;

    public boolean isDateInRange(LocalDateTime dateToCheck, LocalDateTime startDate, LocalDateTime endDate) {
        return dateToCheck.isAfter(startDate) && dateToCheck.isBefore(endDate);
    }

    public Activity registerActivity(ActivityRequest request, Trip trip) {
        Activity newActivity = new Activity(request.title(), request.occurs_at(), trip);

        if (!isDateInRange(newActivity.getOccursAt(), trip.getStartsAt(), trip.getEndsAt()))
            throw new ActivityDateNotInTripRange();

        Activity savedActivity = this.repository.save(newActivity);
        return savedActivity;
    }

    public List<ActivityDTO> getAllActivitiesFromTrip(UUID id) {
        return this.repository.findByTripId(id)
                .stream()
                .map(activity -> new ActivityDTO(activity.getId(), activity.getTitle(), activity.getOccursAt()))
                .toList();
    }
}
