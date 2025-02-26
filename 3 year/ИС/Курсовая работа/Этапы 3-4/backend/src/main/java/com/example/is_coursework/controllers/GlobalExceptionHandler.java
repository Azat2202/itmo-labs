package com.example.is_coursework.controllers;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail httpMessageNotReadableException(final HttpMessageNotReadableException exception) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        errorDetail.setProperty("description", "The request body is incorrect");
        return errorDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(final MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .collect(Collectors.toList());
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(final List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

    @ExceptionHandler({ IllegalStateException.class, BadCredentialsException.class, })
    public ProblemDetail illegalStateException(final IllegalStateException exception) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        errorDetail.setProperty("description", "The username or password is incorrect");
        return errorDetail;
    }

    @ExceptionHandler({ AccountStatusException.class })
    public ProblemDetail accountStatusException(final IllegalStateException exception) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        errorDetail.setProperty("description", "The account is locked");
        return errorDetail;
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ProblemDetail accountDeniedException(final IllegalStateException exception) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        errorDetail.setProperty("description", "You are not authorized to access this resource");
        return errorDetail;
    }

    @ExceptionHandler({ SignatureException.class })
    public ProblemDetail signatureException(final Exception exception) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        errorDetail.setProperty("description", "The JWT signature is invalid");
        return errorDetail;
    }

    @ExceptionHandler({ ExpiredJwtException.class })
    public ProblemDetail expiredJwtException(final IllegalStateException exception) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        errorDetail.setProperty("description", "The JWT token has expired");
        return errorDetail;
    }

    @ExceptionHandler({ JwtException.class })
    public ProblemDetail malformedJwtException(final JwtException exception) {
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        errorDetail.setProperty("description", "The JWT token is invalid");
        return errorDetail;
    }

    @ExceptionHandler({ HttpClientErrorException.class })
    public ProblemDetail malformedJwtException(final HttpClientErrorException exception) {
        return ProblemDetail.forStatusAndDetail(exception.getStatusCode(), exception.getMessage());
    }

    @ExceptionHandler({ Exception.class })
    public ProblemDetail handleSecurityException(final Exception exception) {
        log.error("Internal exception!", exception);
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getMessage());
        errorDetail.setProperty("description", "Unknown internal server error.");
        return errorDetail;
    }

}