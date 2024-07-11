package br.com.anthonycruz.planner.exceptions;

public class StartDateAfterEndDate extends RuntimeException {
    public StartDateAfterEndDate(String message) {
        super(message);
    }
}
