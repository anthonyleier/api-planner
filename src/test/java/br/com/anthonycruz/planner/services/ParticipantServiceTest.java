package br.com.anthonycruz.planner.services;

import br.com.anthonycruz.planner.mocks.MockParticipant;
import br.com.anthonycruz.planner.mocks.MockTrip;
import br.com.anthonycruz.planner.participant.*;
import br.com.anthonycruz.planner.trip.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParticipantServiceTest {
    @Mock
    private ParticipantRepository repository;

    @InjectMocks
    private ParticipantService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterParticipantsToTrip() {
        UUID testTripId = UUID.fromString("33f609fc-004b-4fc2-a635-71d2eae72060");

        List<String> emails = new ArrayList<>();
        emails.add("carolina.mendes@gmail.com");
        emails.add("thiago.almeida92@gmail.com");
        emails.add("julia.pereira88@gmail.com");
        emails.add("felipe.fernandes@gmail.com");
        emails.add("mariana.souza10@gmail.com");

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

        Participant mockEntity = MockParticipant.mockEntityWithEmail(email);
        when(repository.save(any(Participant.class))).thenReturn(mockEntity);

        Trip trip = MockTrip.mockEntity(testTripId);
        ParticipantResponse response = service.registerParticipantToTrip("anthonyleierlw@gmail.com", trip);

        assertNotNull(response);
        assertNotNull(response.id());
    }

    @Test
    public void testTriggerConfirmationEmailToParticipants() {

    }

    @Test
    public void testTriggerConfirmationEmailToParticipant() {

    }

    @Test
    public void testGetAllParticipantsFromTrip() {

    }
}
