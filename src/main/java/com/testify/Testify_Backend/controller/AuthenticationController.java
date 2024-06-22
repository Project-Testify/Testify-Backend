package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.dto.requests.auth.AuthenticationRequest;
import com.testify.Testify_Backend.dto.responses.auth.AuthenticationResponse;
import com.testify.Testify_Backend.dto.requests.auth.RegistrationRequest;
import com.testify.Testify_Backend.dto.responses.auth.RegisterResponse;
import com.testify.Testify_Backend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

//    @PostMapping("/register")
//    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest request){
//        return ResponseEntity.ok(authService.register(request));
//    }

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

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return authService.confirmToken(token);
    }
}
