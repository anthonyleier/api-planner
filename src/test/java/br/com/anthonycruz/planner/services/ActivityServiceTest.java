package br.com.anthonycruz.planner.services;

import br.com.anthonycruz.planner.activity.*;
import br.com.anthonycruz.planner.exceptions.ActivityDateNotInTripRange;
import br.com.anthonycruz.planner.mocks.MockActivity;
import br.com.anthonycruz.planner.mocks.MockTrip;
import br.com.anthonycruz.planner.trip.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ActivityServiceTest {
    @Mock
    ActivityRepository repository;

    @InjectMocks
    ActivityService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIsDateInRange() {
        LocalDateTime dateToCheck1 = LocalDateTime.of(2024, 7, 15, 10, 0);
        LocalDateTime startDate1 = LocalDateTime.of(2024, 7, 20, 10, 0);
        LocalDateTime endDate1 = LocalDateTime.of(2024, 7, 30, 10, 0);
        boolean result1 = service.isDateInRange(dateToCheck1, startDate1, endDate1);
        assertFalse(result1);

        LocalDateTime dateToCheck2 = LocalDateTime.of(2024, 7, 25, 10, 0);
        LocalDateTime startDate2 = LocalDateTime.of(2024, 7, 20, 10, 0);
        LocalDateTime endDate2 = LocalDateTime.of(2024, 7, 30, 10, 0);
        boolean result2 = service.isDateInRange(dateToCheck2, startDate2, endDate2);
        assertTrue(result2);
    }

    @Test
    public void testRegisterActivity() {
        UUID testTripId = UUID.fromString("33f609fc-004b-4fc2-a635-71d2eae72060");
        Trip trip = MockTrip.mockEntity(testTripId);

        Activity activity = MockActivity.mockEntity(LocalDateTime.of(2024, 7, 23, 15, 0));
        when(repository.save(any(Activity.class))).thenReturn(activity);

        ActivityRequest request = MockActivity.mockRequest("2024-07-23T15:00");
        ActivityResponse response = service.registerActivity(request, trip);

        assertNotNull(response);
        assertNotNull(response.id());
    }

    @Test
    public void testRegisterActivityWithWrongDate() {
        UUID testTripId = UUID.fromString("33f609fc-004b-4fc2-a635-71d2eae72060");
        Trip trip = MockTrip.mockEntity(testTripId);

        Activity activity = MockActivity.mockEntity(LocalDateTime.of(2024, 7, 15, 10, 0));
        when(repository.save(any(Activity.class))).thenReturn(activity);

        ActivityRequest request = MockActivity.mockRequest("2000-01-01T13:00");
        Exception exception = assertThrows(ActivityDateNotInTripRange.class,
                () -> service.registerActivity(request, trip));
        assertTrue(exception.getMessage().contains("Activity date must be between trip date range"));
    }

    @Test
    public void testGetAllActivitiesFromTrip() {
        UUID testTripId = UUID.fromString("33f609fc-004b-4fc2-a635-71d2eae72060");

        when(repository.findByTripId(any(UUID.class))).thenReturn(MockActivity.mockEntities(5));
        List<ActivityDTO> activities = service.getAllActivitiesFromTrip(testTripId);

        assertNotNull(activities);
        assertEquals(activities.size(), 5);
        assertNotNull(activities.getFirst().id());
        assertEquals(activities.getFirst().title(), "Passeio ao corcovado 0");
        assertEquals(activities.getFirst().occursAt(), LocalDateTime.of(2024, 7, 23, 15, 0));
    }
}
