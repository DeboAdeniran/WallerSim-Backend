package com.example.walletsim_backend.exception;

import com.example.walletsim_backend.dto.response.ApiResponse;
import com.example.walletsim_backend.dto.response.ErrorDetail;
import com.example.walletsim_backend.dto.response.FieldError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUserNotFound(UserNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), "USER_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse<?>> handleInvalidToken(InvalidTokenException ex) {
        return buildErrorResponse(ex.getMessage(), "TOKEN_INVALID", HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiResponse<?>> handleTokenExpired(TokenExpiredException ex) {
        return buildErrorResponse(ex.getMessage(), "TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(EmailAlreadyVerifiedException.class)
    public ResponseEntity<ApiResponse<?>> handleEmailAlreadyVerified(EmailAlreadyVerifiedException ex) {
        return buildErrorResponse(ex.getMessage(), "EMAIL_ALREADY_VERIFIED", HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleBadRequest(BadRequestException ex) {
        return buildErrorResponse(ex.getMessage(), "BAD_REQUEST", HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> FieldError.builder()
                        .field(err.getField())
                        .message(err.getDefaultMessage())
                        .build())
                .toList();

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .error(ErrorDetail.builder()
                        .code("VALIDATION_ERROR")
                        .details(fieldErrors)
                        .build())
                .timestamp(LocalDateTime.now().toString())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception ex) {
        // Log the stack trace for debugging
        ex.printStackTrace();
        return buildErrorResponse("Internal server error", "INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private ResponseEntity<ApiResponse<?>> buildErrorResponse(String message, String code, HttpStatus status) {
        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .status(status.value())
                .message(message)
                .error(ErrorDetail.builder()
                        .code(code)
                        .details(List.of())
                        .build())
                .timestamp(LocalDateTime.now().toString())
                .build();

        return new ResponseEntity<>(response, status);
    }
}