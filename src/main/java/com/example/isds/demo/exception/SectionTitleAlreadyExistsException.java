package com.example.isds.demo.exception;

public class SectionTitleAlreadyExistsException extends RuntimeException{
    public SectionTitleAlreadyExistsException(String message) {
        super(message);
    }
}
