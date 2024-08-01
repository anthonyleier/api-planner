package br.com.anthonycruz.planner.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.anthonycruz.planner.dtos.ParticipantDTO;
import br.com.anthonycruz.planner.models.Participant;
import br.com.anthonycruz.planner.models.Trip;
import br.com.anthonycruz.planner.repositories.ParticipantRepository;

@Service
public class ParticipantService {
    private final ParticipantRepository repository;
    private final EmailService emailService;

    private ParticipantService(ParticipantRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public List<Participant> registerParticipantsToTrip(List<String> participantsToInvite, Trip trip) {
        List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email, trip)).toList();
        return this.repository.saveAll(participants);
    }

    public Participant registerParticipantToTrip(String email, Trip trip) {
        Participant newParticipant = new Participant(email, trip);
        Participant savedParticipant = this.repository.save(newParticipant);
        return savedParticipant;
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {
        List<Participant> participants = this.repository.findByTripId(tripId);
        for (Participant participant : participants) {
            emailService.sendConfirmationEmail(participant.getId(), participant.getEmail());
        }
    }

    public void triggerConfirmationEmailToParticipant(UUID participantID, String email) {
        emailService.sendConfirmationEmail(participantID, email);
    }

    public List<ParticipantDTO> getAllParticipantsFromTrip(UUID id) {
        return this.repository.findByTripId(id)
                .stream()
                .map(participant -> new ParticipantDTO(participant.getId(), participant.getName(), participant.getEmail(), participant.isConfirmed()))
                .toList();
    }
}
