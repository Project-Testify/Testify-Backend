package com.testify.Testify_Backend.responses.candidate_management;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OrganizationCandidateView {
    private String email;
    private String contact;
    private String firstName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String website;
    private boolean verified;
}
