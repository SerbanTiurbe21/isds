package com.example.isds.demo.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class InterviewException {
    private final String message;
    private final HttpStatus httpStatus;
}
