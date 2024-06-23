package com.testify.Testify_Backend.responses.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.testify.Testify_Backend.requests.auth.RegistrationRequest;
import com.testify.Testify_Backend.responses.ValidatedResponse;
import com.testify.Testify_Backend.enums.UserRole;
import com.testify.Testify_Backend.model.User;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse extends ValidatedResponse<RegistrationRequest> {
    @JsonProperty("success")
    @Builder.Default
    private boolean success = false;
    @JsonProperty("errorMessage")
    private String errorMessage;
    @JsonProperty("loggedUser")
    private User loggedUser;
    @JsonProperty("role")
    private UserRole role;
    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("refreshToken")
    private String refreshToken;
}
