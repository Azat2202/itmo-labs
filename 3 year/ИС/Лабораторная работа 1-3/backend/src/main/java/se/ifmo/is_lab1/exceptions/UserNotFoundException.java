package se.ifmo.is_lab1.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends StudyGroupRuntimeException{
    public UserNotFoundException() {
        super("User with such id not found", HttpStatus.NOT_FOUND);
    }
}
