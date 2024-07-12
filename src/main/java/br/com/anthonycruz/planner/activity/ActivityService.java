package br.com.anthonycruz.planner.activity;

import br.com.anthonycruz.planner.exceptions.ActivityDateNotInTripRange;
import br.com.anthonycruz.planner.exceptions.StartDateAfterEndDate;
import br.com.anthonycruz.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository repository;

    public boolean isDateInRange(LocalDateTime dateToCheck, LocalDateTime startDate, LocalDateTime endDate){
        return dateToCheck.isAfter(startDate) && dateToCheck.isBefore(endDate);
    }

    public ActivityResponse registerActivity(ActivityRequest request, Trip trip) {
        Activity newActivity = new Activity(request.title(), request.occurs_at(), trip);
        if (!isDateInRange(newActivity.getOccursAt(), trip.getStartsAt(), trip.getEndsAt())) throw new ActivityDateNotInTripRange();
        this.repository.save(newActivity);
        return new ActivityResponse(newActivity.getId());
    }

    public List<ActivityDTO> getAllActivitiesFromTrip(UUID id) {
        return this.repository.findByTripId(id).stream().map(activity -> new ActivityDTO(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }
}
