package se.ifmo.is_lab1.exceptions;

import org.springframework.http.HttpStatus;

public class ObjectDontBelongToUserException extends StudyGroupRuntimeException{
    public ObjectDontBelongToUserException() {
        super("StudyGroup object don`t belong to you", HttpStatus.FORBIDDEN);
    }
}
