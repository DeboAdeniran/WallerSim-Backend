package com.example.walletsim_backend.dto.request;

import lombok.Data;

@Data
public class ChangePinRequest {
    //currentPin(String), newPin(String)
    private String currentPin;
    private String newPin;
}
