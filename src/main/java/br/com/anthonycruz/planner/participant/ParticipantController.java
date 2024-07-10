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
    public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID id, @RequestBody ParticipantRequest request) {
        Optional<Participant> participant = this.repository.findById(id);
        if (participant.isPresent()) {
            Participant rawParticipant = participant.get();

            rawParticipant.setConfirmed(true);
            rawParticipant.setName(request.name());

            this.repository.save(rawParticipant);
            return ResponseEntity.ok(rawParticipant);
        }
        return ResponseEntity.notFound().build();
    }
}
