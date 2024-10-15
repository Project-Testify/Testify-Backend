package com.testify.Testify_Backend.requests.auth;

import com.testify.Testify_Backend.enums.UserRole;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RegistrationRequest {
    private String email;
    private String password;
    private String username;
    private String contactNo;
    private UserRole role;

    private String token; //for exam setter

    private String firstName;
    private String lastName;
    private String bio;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String website;

}
