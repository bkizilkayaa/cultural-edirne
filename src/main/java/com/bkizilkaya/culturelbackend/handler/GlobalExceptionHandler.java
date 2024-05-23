package com.bkizilkaya.culturelbackend.handler;

import com.bkizilkaya.culturelbackend.exception.CustomErrorResponse;
import com.bkizilkaya.culturelbackend.exception.NotFoundException;
import com.bkizilkaya.culturelbackend.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    private final HttpServletRequest request;

    public GlobalExceptionHandler(HttpServletRequest request) {
        this.request = request;
    }

    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity<CustomErrorResponse> handleCustomException(ValidationException ex) {
        CustomErrorResponse errorResponse = customErrorResponseBuilder(BAD_REQUEST, ex.getMessage());
        log.error("Validation error occurred! :>> " + ex.getMessage());
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<CustomErrorResponse> handleCustomException(NotFoundException ex) {
        CustomErrorResponse errorResponse = customErrorResponseBuilder(NOT_FOUND, ex.getMessage());
        log.error(ex.getObjectType() + " not found!");
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    private CustomErrorResponse customErrorResponseBuilder(HttpStatus status, String exceptionMessage) {
        return CustomErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(exceptionMessage)
                .path(this.request.getRequestURI()).build();
    }
}
