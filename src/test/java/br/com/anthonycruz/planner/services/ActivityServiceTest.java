package br.com.anthonycruz.planner.services;

import br.com.anthonycruz.planner.activity.ActivityRepository;
import br.com.anthonycruz.planner.activity.ActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        //service.registerActivity();
    }

    @Test
    public void testGetAllActivitiesFromTrip() {
        //service.getAllActivitiesFromTrip();
    }
}
