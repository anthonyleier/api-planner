package br.com.anthonycruz.planner.exceptions.handle;

import br.com.anthonycruz.planner.exceptions.StartDateAfterEndDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class Handle {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllExceptions(Exception exception, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(StartDateAfterEndDate.class)
    public ResponseEntity<?> handleStartDateAfterEndDate(StartDateAfterEndDate exception, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(new Date(), exception.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
