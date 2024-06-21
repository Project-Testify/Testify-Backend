package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.dto.RegistrationRequest;
import com.testify.Testify_Backend.service.RegistrationService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="api/v1/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }
}
