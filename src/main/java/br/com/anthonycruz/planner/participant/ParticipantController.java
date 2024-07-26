package br.com.anthonycruz.planner.participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

    @Autowired
    private ParticipantRepository repository;

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
