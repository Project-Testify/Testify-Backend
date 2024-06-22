package com.testify.Testify_Backend.dto.responses.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.testify.Testify_Backend.dto.requests.auth.AuthenticationRequest;
import com.testify.Testify_Backend.dto.responses.ValidatedResponse;
import com.testify.Testify_Backend.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthenticationResponse extends ValidatedResponse<AuthenticationRequest> {
    @JsonProperty("success")
    @Builder.Default
    private boolean success = false;
    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("refreshToken")
    private String refreshToken;

    // User State fields
    @JsonProperty("id")
    private Long id;
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("role")
    private UserRole role;

}
