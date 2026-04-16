package com.example.walletsim_backend.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private int status;
    private String message;
    private T data;
    private ErrorDetail error;
    private String timestamp;

    public static <T> ApiResponse<T> success(T data, String message, int status) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.status = status;
        response.message = message;
        response.data = data;
        response.timestamp = LocalDateTime.now().toString();
        return response;
    }
    public static <T> ApiResponse<T> error(String message, ErrorDetail error, int status) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.status = status;
        response.message = message;
        response.error = error;
        response.timestamp = LocalDateTime.now().toString();
        return response;
    }

    }
