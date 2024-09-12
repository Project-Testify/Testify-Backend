package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.requests.auth.AuthenticationRequest;
import com.testify.Testify_Backend.requests.auth.OrganisationRegistrationRequest;
import com.testify.Testify_Backend.responses.auth.AuthenticationResponse;
import com.testify.Testify_Backend.requests.auth.RegistrationRequest;
import com.testify.Testify_Backend.responses.auth.RegisterResponse;
import com.testify.Testify_Backend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegistrationRequest request){
        //print request
        System.out.println(request);
        RegisterResponse response = authService.register(request, false);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/authenticate") //login honde
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        AuthenticationResponse response = authService.authenticate(request);
        //if response.setSuccess(true) then return 200 else return 400
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping(path = "confirm")
    public String verifyEmail(@RequestParam("token") String token) {
        return authService.confirmToken(token);
    }

}
