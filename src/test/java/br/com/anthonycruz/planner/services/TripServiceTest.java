package br.com.anthonycruz.planner.services;


import br.com.anthonycruz.planner.mocks.MockTrip;
import br.com.anthonycruz.planner.trip.Trip;
import br.com.anthonycruz.planner.trip.TripRepository;
import br.com.anthonycruz.planner.trip.TripRequest;
import br.com.anthonycruz.planner.trip.TripService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class TripServiceTest {
    @Mock
    TripRepository repository;

    @InjectMocks
    TripService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate() {
        UUID testId = UUID.fromString("638a9640-1775-4455-b055-4703fae21cbe");

        Trip persistedTrip = MockTrip.mockEntity(testId);
        TripRequest requestTrip = MockTrip.mockRequest();

        when(repository.save(any(Trip.class))).thenReturn(persistedTrip);
        var createdTrip = service.create(requestTrip);

        assertNotNull(createdTrip);
        assertNotNull(createdTrip.getId());
        assertEquals(createdTrip.getDestination(), requestTrip.destination());
        assertEquals(createdTrip.getStartsAt().toString(), requestTrip.starts_at());
        assertEquals(createdTrip.getEndsAt().toString(), requestTrip.ends_at());
        assertEquals(createdTrip.getOwnerName(), requestTrip.owner_name());
        assertEquals(createdTrip.getOwnerEmail(), requestTrip.owner_email());
    }
}
