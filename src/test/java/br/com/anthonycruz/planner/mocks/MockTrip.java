package br.com.anthonycruz.planner.mocks;

import br.com.anthonycruz.planner.trip.Trip;
import br.com.anthonycruz.planner.trip.TripRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MockTrip {
    public static Trip mockEntity(UUID testId) {
        Trip trip = new Trip();
        trip.setId(testId);
        trip.setDestination("São Paulo/SP");
        trip.setStartsAt(LocalDateTime.parse("2024-07-20T13:00:00Z", DateTimeFormatter.ISO_DATE_TIME));
        trip.setEndsAt(LocalDateTime.parse("2024-07-25T20:00:00Z", DateTimeFormatter.ISO_DATE_TIME));
        trip.setConfirmed(false);
        trip.setOwnerEmail("anthonyleierlw@gmail.com");
        trip.setOwnerName("Anthony Cruz");
        return trip;
    }

    public static TripRequest mockRequest(String destination, String startsAt, String endsAt) {
        List<String> emails = new ArrayList<>();
        emails.add("john.doe@gmail.com");
        emails.add("jane.smith@gmail.com");
        emails.add("mike.jones@gmail.com");

        return new TripRequest(
                destination,
                startsAt,
                endsAt,
                emails,
                "anthonyleierlw@gmail.com",
                "Anthony Cruz"
        );
    }
}
