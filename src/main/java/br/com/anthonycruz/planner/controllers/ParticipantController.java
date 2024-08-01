package br.com.anthonycruz.planner.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.anthonycruz.planner.dtos.ParticipantDTO;
import br.com.anthonycruz.planner.models.Participant;
import br.com.anthonycruz.planner.repositories.ParticipantRepository;
import br.com.anthonycruz.planner.requests.ParticipantRequest;

@RestController
@RequestMapping("/participants")
public class ParticipantController {
    private final ParticipantRepository repository;

    public ParticipantController(ParticipantRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ParticipantDTO> confirmParticipant(@PathVariable UUID id, @RequestBody ParticipantRequest request) {
        Optional<Participant> optionalParticipant = this.repository.findById(id);

        if (optionalParticipant.isPresent()) {
            Participant participant = optionalParticipant.get();

            participant.setConfirmed(true);
            participant.setName(request.name());

            Participant savedParticipant = this.repository.save(participant);
            ParticipantDTO participantDTO = new ParticipantDTO(savedParticipant.getId(), savedParticipant.getName(), savedParticipant.getEmail(), savedParticipant.isConfirmed());

            return ResponseEntity.ok(participantDTO);
        }

        return ResponseEntity.notFound().build();
    }
}