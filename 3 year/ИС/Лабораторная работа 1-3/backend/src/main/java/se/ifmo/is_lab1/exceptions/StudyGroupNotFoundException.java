package se.ifmo.is_lab1.exceptions;

import org.springframework.http.HttpStatus;

public class StudyGroupNotFoundException extends StudyGroupRuntimeException{
    public StudyGroupNotFoundException() {
        super("StudyGroup not found!", HttpStatus.NOT_FOUND);
    }
}
