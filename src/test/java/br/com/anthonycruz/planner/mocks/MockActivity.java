package br.com.anthonycruz.planner.mocks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.anthonycruz.planner.models.Activity;
import br.com.anthonycruz.planner.requests.ActivityRequest;

public class MockActivity {
    public static List<Activity> mockEntities(int qty) {
        List<Activity> activities = new ArrayList<>();
        for (int i = 0; i < qty; i++) {
            Activity activity = new Activity();
            activity.setId(UUID.randomUUID());
            activity.setTitle("Passeio ao corcovado " + i);
            activity.setOccursAt(LocalDateTime.of(2024, 7, 23, 15, 0));
            activities.add(activity);
        }
        return activities;
    }

    public static ActivityRequest mockRequest(String occursAt) {
        return new ActivityRequest("Google", occursAt);
    }

    public static Activity mockEntity(LocalDateTime occursAt) {
        Activity activity = new Activity();
        activity.setId(UUID.randomUUID());
        activity.setTitle("Passeio ao corcovado");
        activity.setOccursAt(occursAt);
        return activity;
    }
}
