package br.com.anthonycruz.planner.trip;

import br.com.anthonycruz.planner.activity.ActivityDTO;
import br.com.anthonycruz.planner.activity.ActivityRequest;
import br.com.anthonycruz.planner.activity.ActivityResponse;
import br.com.anthonycruz.planner.activity.ActivityService;
import br.com.anthonycruz.planner.link.LinkDTO;
import br.com.anthonycruz.planner.link.LinkRequest;
import br.com.anthonycruz.planner.link.LinkResponse;
import br.com.anthonycruz.planner.link.LinkService;
import br.com.anthonycruz.planner.participant.ParticipantDTO;
import br.com.anthonycruz.planner.participant.ParticipantRequest;
import br.com.anthonycruz.planner.participant.ParticipantResponse;
import br.com.anthonycruz.planner.participant.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private TripService service;

    @Autowired
    private ParticipantService participantService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private LinkService linkService;

    @PostMapping
    public ResponseEntity<TripResponse> create(@RequestBody TripRequest request) {
        Trip trip = this.service.create(request);
        this.participantService.registerParticipantsToTrip(request.emails_to_invite(), trip);

        TripResponse response = new TripResponse(trip.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
        Optional<Trip> trip = this.service.findById(id);
        return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequest request) {
        Optional<Trip> trip = this.service.findById(id);
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            var destination = request.destination();
            var startsAt = LocalDateTime.parse(request.starts_at(), DateTimeFormatter.ISO_DATE_TIME);
            var endsAt = LocalDateTime.parse(request.ends_at(), DateTimeFormatter.ISO_DATE_TIME);

            Trip updatedTrip = this.service.update(rawTrip, destination, startsAt, endsAt);
            return ResponseEntity.ok(updatedTrip);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
        Optional<Trip> trip = this.service.findById(id);
        if (trip.isPresent()) {
            Trip rawTrip = trip.get();

            Trip updatedTrip = this.service.confirm(rawTrip);
            this.participantService.triggerConfirmationEmailToParticipants(rawTrip.getId());

            return ResponseEntity.ok(updatedTrip);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantResponse> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequest request) {
        Optional<Trip> trip = this.service.findById(id);
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
        Optional<Trip> trip = this.service.findById(id);
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
        Optional<Trip> trip = this.service.findById(id);
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
