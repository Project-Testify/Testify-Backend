package com.testify.Testify_Backend.dto;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
    private String email;
    private String password;
}
