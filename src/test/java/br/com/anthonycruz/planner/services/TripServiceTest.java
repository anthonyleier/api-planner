package br.com.anthonycruz.planner.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.anthonycruz.planner.exceptions.StartDateAfterEndDate;
import br.com.anthonycruz.planner.mocks.MockTrip;
import br.com.anthonycruz.planner.models.Trip;
import br.com.anthonycruz.planner.repositories.TripRepository;
import br.com.anthonycruz.planner.requests.TripRequest;

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
        TripRequest requestTrip = MockTrip.mockRequest("São Paulo/SP", "2024-07-20T13:00", "2024-07-25T20:00");

        when(repository.save(any(Trip.class))).thenReturn(persistedTrip);
        var createdTrip = service.create(requestTrip);

        assertNotNull(createdTrip);
        assertNotNull(createdTrip.getId());
        assertEquals(createdTrip.getDestination(), requestTrip.destination());
        assertEquals(createdTrip.getStartsAt().toString(), requestTrip.starts_at());
        assertEquals(createdTrip.getEndsAt().toString(), requestTrip.ends_at());
        assertEquals(createdTrip.getOwnerName(), requestTrip.owner_name());
        assertEquals(createdTrip.getOwnerEmail(), requestTrip.owner_email());
        assertFalse(createdTrip.isConfirmed());
    }

    @Test
    public void testCreateWithWrongDate() {
        UUID testId = UUID.fromString("638a9640-1775-4455-b055-4703fae21cbe");

        Trip persistedTrip = MockTrip.mockEntity(testId);
        TripRequest requestTrip = MockTrip.mockRequest("São Paulo/SP", "2030-08-26T13:00", "2024-07-25T20:00");

        when(repository.save(any(Trip.class))).thenReturn(persistedTrip);

        Exception exception = assertThrows(StartDateAfterEndDate.class, () -> service.create(requestTrip));
        assertTrue(exception.getMessage().contains("StartsAt must be before EndsAt"));
    }

    @Test
    public void testUpdate() {
        UUID testId = UUID.fromString("638a9640-1775-4455-b055-4703fae21cbe");

        String newDestination = "Florianópolis/SC";
        LocalDateTime newStartsAt = LocalDateTime.now();
        LocalDateTime newEndsAt = LocalDateTime.now().plusDays(1);

        Trip persistedTrip = MockTrip.mockEntity(testId);
        persistedTrip.setDestination(newDestination);
        persistedTrip.setStartsAt(newStartsAt);
        persistedTrip.setEndsAt(newEndsAt);

        TripRequest requestTrip = MockTrip.mockRequest(newDestination, newStartsAt.toString(), newEndsAt.toString());
        when(repository.save(any(Trip.class))).thenReturn(persistedTrip);
        var updatedTrip = service.update(persistedTrip, newDestination, newStartsAt, newEndsAt);

        assertNotNull(updatedTrip);
        assertNotNull(updatedTrip.getId());
        assertEquals(updatedTrip.getDestination(), requestTrip.destination());
        assertEquals(updatedTrip.getStartsAt().toString(), requestTrip.starts_at());
        assertEquals(updatedTrip.getEndsAt().toString(), requestTrip.ends_at());
        assertEquals(updatedTrip.getOwnerName(), requestTrip.owner_name());
        assertEquals(updatedTrip.getOwnerEmail(), requestTrip.owner_email());
        assertFalse(updatedTrip.isConfirmed());
    }

    @Test
    public void testFindById() {
        UUID testId = UUID.fromString("638a9640-1775-4455-b055-4703fae21cbe");

        Trip persistedTrip = MockTrip.mockEntity(testId);
        TripRequest requestTrip = MockTrip.mockRequest("São Paulo/SP", "2024-07-20T13:00", "2024-07-25T20:00");

        when(repository.findById(testId)).thenReturn(Optional.of(persistedTrip));
        var optionalFoundTrip = service.findById(testId);

        assertTrue(optionalFoundTrip.isPresent());
        var foundTrip = optionalFoundTrip.get();

        assertNotNull(foundTrip);
        assertNotNull(foundTrip.getId());
        assertEquals(foundTrip.getDestination(), requestTrip.destination());
        assertEquals(foundTrip.getStartsAt().toString(), requestTrip.starts_at());
        assertEquals(foundTrip.getEndsAt().toString(), requestTrip.ends_at());
        assertEquals(foundTrip.getOwnerName(), requestTrip.owner_name());
        assertEquals(foundTrip.getOwnerEmail(), requestTrip.owner_email());
        assertFalse(foundTrip.isConfirmed());
    }

    @Test
    public void testConfirm() {
        UUID testId = UUID.fromString("638a9640-1775-4455-b055-4703fae21cbe");

        Trip persistedTrip = MockTrip.mockEntity(testId);
        persistedTrip.setConfirmed(true);

        TripRequest requestTrip = MockTrip.mockRequest("São Paulo/SP", "2024-07-20T13:00", "2024-07-25T20:00");
        when(repository.save(any(Trip.class))).thenReturn(persistedTrip);
        var updatedTrip = service.confirm(persistedTrip);

        assertNotNull(updatedTrip);
        assertNotNull(updatedTrip.getId());
        assertEquals(updatedTrip.getDestination(), requestTrip.destination());
        assertEquals(updatedTrip.getStartsAt().toString(), requestTrip.starts_at());
        assertEquals(updatedTrip.getEndsAt().toString(), requestTrip.ends_at());
        assertEquals(updatedTrip.getOwnerName(), requestTrip.owner_name());
        assertEquals(updatedTrip.getOwnerEmail(), requestTrip.owner_email());
        assertTrue(updatedTrip.isConfirmed());
    }
}
