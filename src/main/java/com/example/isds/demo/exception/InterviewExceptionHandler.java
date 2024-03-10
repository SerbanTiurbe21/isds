package com.example.isds.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InterviewExceptionHandler {
    @ExceptionHandler(value ={InvalidSectionTitleException.class})
    public ResponseEntity<Object> handleInvalidSectionTitleException(InvalidSectionTitleException exception){
        InterviewException interviewException = new InterviewException(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(interviewException, interviewException.getHttpStatus());
    }

    @ExceptionHandler(value ={InterviewNotFoundException.class})
    public ResponseEntity<Object> handleInterviewNotFoundException(InterviewNotFoundException exception){
        InterviewException interviewException = new InterviewException(
                exception.getMessage(),
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(interviewException, interviewException.getHttpStatus());
    }

    @ExceptionHandler(value ={InterviewScoreDocumentAlreadyExistsException.class})
    public ResponseEntity<Object> handleInterviewDocumentAlreadyExistsException(InterviewScoreDocumentAlreadyExistsException exception){
        InterviewException interviewException = new InterviewException(
                exception.getMessage(),
                HttpStatus.CONFLICT
        );
        return new ResponseEntity<>(interviewException, interviewException.getHttpStatus());
    }
}
