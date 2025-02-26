package se.ifmo.is_lab1.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class StudyGroupRuntimeException extends RuntimeException {

    private final HttpStatus httpStatus;

    public StudyGroupRuntimeException(final String message, final HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
