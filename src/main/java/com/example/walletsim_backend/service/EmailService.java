package com.example.walletsim_backend.service;

import com.example.walletsim_backend.dto.request.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
