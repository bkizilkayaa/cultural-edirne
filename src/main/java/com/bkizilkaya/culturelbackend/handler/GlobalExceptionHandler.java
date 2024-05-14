package com.bkizilkaya.culturelbackend.handler;

import com.bkizilkaya.culturelbackend.exception.CustomErrorResponse;
import com.bkizilkaya.culturelbackend.exception.NotFoundException;
import com.bkizilkaya.culturelbackend.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final HttpServletRequest request;

    public GlobalExceptionHandler(HttpServletRequest request) {
        this.request = request;
    }

    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity<CustomErrorResponse> handleCustomException(ValidationException ex) {
        CustomErrorResponse errorResponse =  customErrorResponseBuilder(BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(errorResponse, BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<CustomErrorResponse> handleCustomException(NotFoundException ex) {
        CustomErrorResponse errorResponse = customErrorResponseBuilder(NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(errorResponse, NOT_FOUND);
    }

    private CustomErrorResponse customErrorResponseBuilder(HttpStatus status, String exceptionMessage){
        return  CustomErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(exceptionMessage)
                .path(this.request.getRequestURI()).build();
    }
}
