package br.com.anthonycruz.planner.exceptions;

public class StartDateAfterEndDate extends RuntimeException {
    public StartDateAfterEndDate() {
        super("StartsAt must be before EndsAt");
    }
}
