package se.ifmo.is_lab1.exceptions;

import org.springframework.http.HttpStatus;

public class ObjectIsNotEditable extends StudyGroupRuntimeException{
    public ObjectIsNotEditable() {
        super("Object is not editable", HttpStatus.FORBIDDEN);
    }
}
