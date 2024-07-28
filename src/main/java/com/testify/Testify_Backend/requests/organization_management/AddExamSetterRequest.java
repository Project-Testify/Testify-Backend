package com.testify.Testify_Backend.requests.organization_management;

import com.testify.Testify_Backend.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AddExamSetterRequest {
    private String email;
    private String password;
    private String username;
    private String contactNo;
    private UserRole role;


    private String firstName;
    private String lastName;
    private String bio;

}
