package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.dto.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    public String register(RegistrationRequest request) {
        return "works";
    }
}
