package br.com.anthonycruz.planner.exceptions.handle;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String details) {
}
