package com.testify.Testify_Backend.responses.candidate_management;

import com.testify.Testify_Backend.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CandidateProfile {
    private String email;
    private String username;
    private String contactNo;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String bio;
}
