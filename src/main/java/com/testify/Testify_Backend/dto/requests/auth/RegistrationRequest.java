package com.testify.Testify_Backend.dto.requests.auth;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RegistrationRequest {
    private String email;
    private String password;
}
