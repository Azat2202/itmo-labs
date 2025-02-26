package se.ifmo.is_lab1.exceptions;

import org.springframework.http.HttpStatus;

public class LocationNotFoundException extends StudyGroupRuntimeException{
    public LocationNotFoundException() {
        super("Location not found!", HttpStatus.NOT_FOUND);
    }
}
