package br.com.anthonycruz.planner.mocks;

import br.com.anthonycruz.planner.participant.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MockParticipant {
    public static Participant mockEntityWithEmail(String email) {
        Participant participant = new Participant();
        participant.setId(UUID.randomUUID());
        participant.setName("");
        participant.setEmail(email);
        participant.setConfirmed(false);
        return participant;
    }

    public static List<Participant> mockEntitiesWithEmails(List<String> emails) {
        List<Participant> participants = new ArrayList<>();
        for (String email : emails) {
            Participant participant = mockEntityWithEmail(email);
            participants.add(participant);
        }
        return participants;
    }
}
