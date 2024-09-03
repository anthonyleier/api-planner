package br.com.anthonycruz.planner.exceptions;

public class PhotoNotFoundException extends RuntimeException {
    public PhotoNotFoundException(String ex) {
        super(ex);
    }

    public PhotoNotFoundException(String ex, Throwable cause) {
        super(ex, cause);
    }
}
