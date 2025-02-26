package se.ifmo.is_lab1.exceptions;

import org.springframework.http.HttpStatus;

public class PersonNotFoundException extends StudyGroupRuntimeException{
    public PersonNotFoundException() {
        super("Person not found!", HttpStatus.NOT_FOUND);
    }
}
