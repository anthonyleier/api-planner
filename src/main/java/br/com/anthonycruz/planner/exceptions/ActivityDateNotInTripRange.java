package br.com.anthonycruz.planner.exceptions;

public class ActivityDateNotInTripRange extends RuntimeException {
    public ActivityDateNotInTripRange() {
        super("Activity date must be between trip date range");
    }
}
