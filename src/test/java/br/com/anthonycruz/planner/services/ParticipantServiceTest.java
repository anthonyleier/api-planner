package br.com.anthonycruz.planner.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.anthonycruz.planner.mocks.MockParticipant;
import br.com.anthonycruz.planner.mocks.MockTrip;
import br.com.anthonycruz.planner.models.Participant;
import br.com.anthonycruz.planner.models.Trip;
import br.com.anthonycruz.planner.repositories.ParticipantRepository;

public class ParticipantServiceTest {
    private static final List<String> emails = new ArrayList<>();

    @Mock
    private ParticipantRepository repository;

    @InjectMocks
    private ParticipantService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeAll
    public static void setUpClass() {
        emails.add("carolina.mendes@gmail.com");
        emails.add("thiago.almeida92@gmail.com");
        emails.add("julia.pereira88@gmail.com");
        emails.add("felipe.fernandes@gmail.com");
        emails.add("mariana.souza10@gmail.com");
    }

    @Test
    public void testRegisterParticipantsToTrip() {
        UUID testTripId = UUID.fromString("33f609fc-004b-4fc2-a635-71d2eae72060");

        List<Participant> participants = MockParticipant.mockEntitiesWithEmails(emails);
        when(repository.saveAll(anyList())).thenReturn(participants);

        Trip trip = MockTrip.mockEntity(testTripId);
        List<Participant> savedParticipants = service.registerParticipantsToTrip(emails, trip);

        assertNotNull(savedParticipants);
        assertEquals(participants.size(), savedParticipants.size());
        assertEquals(participants.getFirst().getEmail(), savedParticipants.getFirst().getEmail());
    }

    @Test
    public void testRegisterParticipantToTrip() {
        String email = "amanda.silva83@gmail.com";
        UUID testTripId = UUID.fromString("33f609fc-004b-4fc2-a635-71d2eae72060");

        Participant participant = MockParticipant.mockEntityWithEmail(email);
        when(repository.save(any(Participant.class))).thenReturn(participant);

        Trip trip = MockTrip.mockEntity(testTripId);
        Participant savedParticipant = service.registerParticipantToTrip(email, trip);

        assertNotNull(savedParticipant.getId());
        assertEquals(participant.getName(), savedParticipant.getName());
        assertEquals(participant.getEmail(), savedParticipant.getEmail());
        assertFalse(participant.isConfirmed());
    }

    @Test
    public void testTriggerConfirmationEmailToParticipants() {
    }

    @Test
    public void testTriggerConfirmationEmailToParticipant() {
    }

    @Test
    public void testGetAllParticipantsFromTrip() {
        UUID testTripId = UUID.fromString("33f609fc-004b-4fc2-a635-71d2eae72060");

        when(repository.findByTripId(any(UUID.class))).thenReturn(MockParticipant.mockEntitiesWithEmails(emails));
        var participants = service.getAllParticipantsFromTrip(testTripId);

        assertNotNull(participants);
        assertEquals(participants.size(), 5);
        assertEquals(participants.getFirst().email(), "carolina.mendes@gmail.com");
    }
}
