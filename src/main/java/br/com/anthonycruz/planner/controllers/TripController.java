package br.com.anthonycruz.planner.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.anthonycruz.planner.dtos.ActivityDTO;
import br.com.anthonycruz.planner.dtos.LinkDTO;
import br.com.anthonycruz.planner.dtos.ParticipantDTO;
import br.com.anthonycruz.planner.dtos.PhotoDTO;
import br.com.anthonycruz.planner.dtos.TripDTO;
import br.com.anthonycruz.planner.models.Activity;
import br.com.anthonycruz.planner.models.Link;
import br.com.anthonycruz.planner.models.Participant;
import br.com.anthonycruz.planner.models.Trip;
import br.com.anthonycruz.planner.requests.ActivityRequest;
import br.com.anthonycruz.planner.requests.LinkRequest;
import br.com.anthonycruz.planner.requests.ParticipantRequest;
import br.com.anthonycruz.planner.requests.TripRequest;
import br.com.anthonycruz.planner.services.ActivityService;
import br.com.anthonycruz.planner.services.LinkService;
import br.com.anthonycruz.planner.services.ParticipantService;
import br.com.anthonycruz.planner.services.PhotoService;
import br.com.anthonycruz.planner.services.TripService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/trips")
public class TripController {
    private final TripService service;
    private final ParticipantService participantService;
    private final ActivityService activityService;
    private final LinkService linkService;
    private final PhotoService photoService;

    public TripController(TripService service, ParticipantService participantService, ActivityService activityService, LinkService linkService, PhotoService photoService) {
        this.service = service;
        this.participantService = participantService;
        this.activityService = activityService;
        this.linkService = linkService;
        this.photoService = photoService;
    }

    @PostMapping
    public ResponseEntity<TripDTO> create(@RequestBody TripRequest request) {
        Trip trip = this.service.create(request);
        TripDTO tripDTO = new TripDTO(
                trip.getId(),
                trip.getDestination(),
                trip.getStartsAt(),
                trip.getEndsAt(),
                trip.isConfirmed(),
                trip.getOwnerName(),
                trip.getOwnerEmail());
        this.participantService.registerParticipantsToTrip(request.emails_to_invite(), trip);
        return new ResponseEntity<>(tripDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripDTO> getTripDetails(@PathVariable UUID id) {
        Optional<Trip> optionalTrip = this.service.findById(id);

        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();

            TripDTO tripDTO = new TripDTO(
                    trip.getId(),
                    trip.getDestination(),
                    trip.getStartsAt(),
                    trip.getEndsAt(),
                    trip.isConfirmed(),
                    trip.getOwnerName(),
                    trip.getOwnerEmail());

            return ResponseEntity.ok(tripDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TripDTO> updateTrip(@PathVariable UUID id, @RequestBody TripRequest request) {
        Optional<Trip> optionalTrip = this.service.findById(id);

        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();

            var destination = request.destination();
            var startsAt = LocalDateTime.parse(request.starts_at(), DateTimeFormatter.ISO_DATE_TIME);
            var endsAt = LocalDateTime.parse(request.ends_at(), DateTimeFormatter.ISO_DATE_TIME);

            Trip updatedTrip = this.service.update(trip, destination, startsAt, endsAt);

            TripDTO tripDTO = new TripDTO(
                    updatedTrip.getId(),
                    updatedTrip.getDestination(),
                    updatedTrip.getStartsAt(),
                    updatedTrip.getEndsAt(),
                    updatedTrip.isConfirmed(),
                    updatedTrip.getOwnerName(),
                    updatedTrip.getOwnerEmail());

            return ResponseEntity.ok(tripDTO);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/confirm")
    public ResponseEntity<TripDTO> confirmTrip(@PathVariable UUID id) {
        Optional<Trip> optionalTrip = this.service.findById(id);

        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();

            Trip updatedTrip = this.service.confirm(trip);
            this.participantService.triggerConfirmationEmailToParticipants(trip.getId());

            TripDTO tripDTO = new TripDTO(
                    updatedTrip.getId(),
                    updatedTrip.getDestination(),
                    updatedTrip.getStartsAt(),
                    updatedTrip.getEndsAt(),
                    updatedTrip.isConfirmed(),
                    updatedTrip.getOwnerName(),
                    updatedTrip.getOwnerEmail());

            return ResponseEntity.ok(tripDTO);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/invite")
    public ResponseEntity<ParticipantDTO> inviteParticipant(@PathVariable UUID id, @RequestBody ParticipantRequest request) {
        Optional<Trip> optionalTrip = this.service.findById(id);

        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();

            Participant participant = this.participantService.registerParticipantToTrip(request.email(), trip);
            ParticipantDTO participantDTO = new ParticipantDTO(
                    participant.getId(),
                    participant.getName(),
                    participant.getEmail(),
                    participant.isConfirmed());

            if (trip.isConfirmed())
                this.participantService.triggerConfirmationEmailToParticipant(participant.getId(), participant.getEmail());

            return ResponseEntity.ok(participantDTO);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ParticipantDTO>> getAllParticipants(@PathVariable UUID id) {
        List<ParticipantDTO> participants = this.participantService.getAllParticipantsFromTrip(id);
        return ResponseEntity.ok(participants);
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityDTO> registerActivity(@PathVariable UUID id, @RequestBody ActivityRequest request) {
        Optional<Trip> optionalTrip = this.service.findById(id);

        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();

            Activity activity = this.activityService.registerActivity(request, trip);
            ActivityDTO activityDTO = new ActivityDTO(activity.getId(), activity.getTitle(), activity.getOccursAt());

            return ResponseEntity.ok(activityDTO);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityDTO>> getAllActivities(@PathVariable UUID id) {
        List<ActivityDTO> activities = this.activityService.getAllActivitiesFromTrip(id);
        return ResponseEntity.ok(activities);
    }

    @PostMapping("/{id}/links")
    public ResponseEntity<LinkDTO> registerLink(@PathVariable UUID id, @RequestBody LinkRequest request) {
        Optional<Trip> optionalTrip = this.service.findById(id);

        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();

            Link link = this.linkService.registerLink(request, trip);
            LinkDTO linkDTO = new LinkDTO(link.getId(), link.getTitle(), link.getUrl());

            return ResponseEntity.ok(linkDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/links")
    public ResponseEntity<List<LinkDTO>> getAllLinks(@PathVariable UUID id) {
        List<LinkDTO> links = this.linkService.getAllLinksFromTrip(id);
        return ResponseEntity.ok(links);
    }

    @PostMapping("/{id}/photos")
    public ResponseEntity<PhotoDTO> uploadPhoto(@PathVariable UUID id, MultipartFile file) {
        Optional<Trip> optionalTrip = this.service.findById(id);

        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();

            String filename = this.photoService.upload(file, trip);
            PhotoDTO photoDTO = new PhotoDTO(filename);

            return ResponseEntity.ok(photoDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/photos/{filename:.+}")
    public ResponseEntity<Resource> downloadPhoto(@PathVariable UUID id, @PathVariable String filename, HttpServletRequest request) {
        Optional<Trip> optionalTrip = this.service.findById(id);

        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();

            String contentType = "";
            Resource resource = this.photoService.load(filename, trip);

            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
                if (contentType.isBlank()) contentType = "application/octet-stream";
            } catch (IOException e) {
                e.printStackTrace();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/photos")
    public ResponseEntity<List<PhotoDTO>> getAllPhotos(@PathVariable UUID id) {
        List<PhotoDTO> photos = this.photoService.getAllPhotosFromTrip(id);
        return ResponseEntity.ok(photos);
    }

    @DeleteMapping("/{id}/photos/{filename:.+}")
    public ResponseEntity<Resource> deletePhoto(@PathVariable UUID id, @PathVariable String filename) {
        Optional<Trip> optionalTrip = this.service.findById(id);

        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();
            this.photoService.delete(filename, trip);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
