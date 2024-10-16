package com.testify.Testify_Backend.responses.admin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrganizationGroupResponse {
    private Long id;
    private String firstName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String website;
    private String profileImage;
}
