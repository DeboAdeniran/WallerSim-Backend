package com.example.walletsim_backend.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class EmailAlreadyVerifiedException extends RuntimeException {
    public EmailAlreadyVerifiedException(String message) {
        super(message);
    }
}