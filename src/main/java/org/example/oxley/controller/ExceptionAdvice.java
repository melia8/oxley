package org.example.oxley.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionAdvice {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorResponse {

        private int statusCode;
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }

    @ExceptionHandler(value = {NoSuchElementException.class, NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse noRecordFound(Exception ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Could not find object");
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequest(Exception ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad request");
    }
}
