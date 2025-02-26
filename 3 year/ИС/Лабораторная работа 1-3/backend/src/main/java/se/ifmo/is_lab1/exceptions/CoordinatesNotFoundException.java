package se.ifmo.is_lab1.exceptions;

import org.springframework.http.HttpStatus;

public class CoordinatesNotFoundException extends StudyGroupRuntimeException{

    public CoordinatesNotFoundException() {
        super("Coordinates not found!", HttpStatus.NOT_FOUND);
    }
}
