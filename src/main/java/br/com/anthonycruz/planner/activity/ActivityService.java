package br.com.anthonycruz.planner.activity;

import br.com.anthonycruz.planner.participant.ParticipantDTO;
import br.com.anthonycruz.planner.trip.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepository repository;

    public ActivityResponse registerActivity(ActivityRequestPayload payload, Trip trip) {
        Activity newActivity = new Activity(payload.title(), payload.occurs_at(), trip);
        this.repository.save(newActivity);
        return new ActivityResponse(newActivity.getId());
    }

    public List<ActivityDTO> getAllActivitiesFromTrip(UUID id) {
        return this.repository.findByTripId(id).stream().map(activity -> new ActivityDTO(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }
}
