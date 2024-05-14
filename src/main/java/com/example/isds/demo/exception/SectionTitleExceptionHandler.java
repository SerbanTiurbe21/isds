package com.example.isds.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SectionTitleExceptionHandler {
    @ExceptionHandler(value = {SectionTitleAlreadyExistsException.class})
    public ResponseEntity<Object> handleSectionTitleAlreadyExistsException(SectionTitleAlreadyExistsException exception) {
        SectionTitleException sectionTitleException = new SectionTitleException(
                exception.getMessage(),
                HttpStatus.CONFLICT
        );
        return new ResponseEntity<>(sectionTitleException, sectionTitleException.getHttpStatus());
    }
}
