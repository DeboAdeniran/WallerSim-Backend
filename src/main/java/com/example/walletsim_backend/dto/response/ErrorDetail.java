package com.example.walletsim_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetail {
    private String code;
    private List<FieldError> details;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<FieldError> getDetails() {
        return details;
    }

    public void setDetails(List<FieldError> details) {
        this.details = details;
    }
}
