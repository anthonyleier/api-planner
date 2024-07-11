package br.com.anthonycruz.planner.trip;

import br.com.anthonycruz.planner.activity.ActivityDTO;
import br.com.anthonycruz.planner.activity.ActivityRequest;
import br.com.anthonycruz.planner.activity.ActivityResponse;
import br.com.anthonycruz.planner.activity.ActivityService;
import br.com.anthonycruz.planner.link.LinkDTO;
import br.com.anthonycruz.planner.link.LinkRequest;
import br.com.anthonycruz.planner.link.LinkResponse;
import br.com.anthonycruz.planner.link.LinkService;
import br.com.anthonycruz.planner.participant.ParticipantResponse;
import br.com.anthonycruz.planner.participant.ParticipantDTO;
import br.com.anthonycruz.planner.participant.ParticipantRequest;
import br.com.anthonycruz.planner.participant.ParticipantService;
import br.com.anthonycruz.planner.exceptions.StartDateAfterEndDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/trips")
public class TripController {
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LinkService linkService;

    @Autowired
    private TripRepository repository;

    @PostMapping
    public ResponseEntity<TripResponse> createTrip(@RequestBody TripRequest request) {
        Trip newTrip = new Trip(request);
        if (newTrip.getStartsAt().isAfter(newTrip.getEndsAt())) throw new StartDateAfterEndDate("StartsAt must be before EndsAt");
        this.repository.save(newTrip);
        this.participantService.registerParticipantsToTrip(request.emails_to_invite(), newTrip);
        return ResponseEntity.ok(new TripResponse(newTrip.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        Optional<Trip> trip = this.repository.findById(id);
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequest request) {
        Optional<Trip> trip = this.repository.findById(id);
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setDestination(request.destination());
            rawTrip.setStartsAt(LocalDateTime.parse(request.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
            rawTrip.setEndsAt(LocalDateTime.parse(request.ends_at(), DateTimeFormatter.ISO_DATE_TIME));

            this.repository.save(rawTrip);
            return ResponseEntity.ok(rawTrip);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        Optional<Trip> trip = this.repository.findById(id);
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();
            rawTrip.setConfirmed(true);

            this.repository.save(rawTrip);
            this.participantService.triggerConfirmationEmailToParticipants(rawTrip.getId());

            return ResponseEntity.ok(rawTrip);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequest request) {
        Optional<Trip> trip = this.repository.findById(id);
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            ParticipantResponse participantResponse = this.participantService.registerParticipantToTrip(request.email(), rawTrip);
            if (rawTrip.isConfirmed()) this.participantService.triggerConfirmationEmailToParticipant(request.email());

            return ResponseEntity.ok(participantResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantDTO>> getAllParticipants(@PathVariable UUID id) {
        List<ParticipantDTO> participants = this.participantService.getAllParticipantsFromTrip(id);
        return ResponseEntity.ok(participants);
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id, @RequestBody ActivityRequest request) {
        Optional<Trip> trip = this.repository.findById(id);
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            ActivityResponse response = this.activityService.registerActivity(request, rawTrip);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityDTO>> getAllActivities(@PathVariable UUID id) {
        List<ActivityDTO> activities = this.activityService.getAllActivitiesFromTrip(id);
        return ResponseEntity.ok(activities);
    }

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id, @RequestBody LinkRequest request) {
        Optional<Trip> trip = this.repository.findById(id);
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            LinkResponse response = this.linkService.registerLink(request, rawTrip);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkDTO>> getAllLinks(@PathVariable UUID id) {
        List<LinkDTO> links = this.linkService.getAllLinksFromTrip(id);
        return ResponseEntity.ok(links);
    }
}
