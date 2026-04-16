package com.example.walletsim_backend.exception;

import com.example.walletsim_backend.dto.response.ApiResponse;
import com.example.walletsim_backend.dto.response.ErrorDetail;
import com.example.walletsim_backend.dto.response.FieldError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFoundException(UserNotFoundException ex) {
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .error(ErrorDetail.builder()
                        .code("USER_NOT_FOUND")
                        .details(List.of(
                                FieldError.builder()
                                        .field("user")
                                        .message(ex.getMessage())
                                        .build()
                        ))
                        .build())
                .timestamp(LocalDateTime.now().toString())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
