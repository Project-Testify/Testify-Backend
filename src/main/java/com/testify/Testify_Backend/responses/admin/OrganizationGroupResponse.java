package com.testify.Testify_Backend.responses.admin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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

    private String verificationDocument01Url;
    private String verificationDocument02Url;
    private String verificationDocument03Url;
    private String verificationDocument04Url;
    private String verificationDocument05Url;


    private String verificationStatus;
    private String rejectionReason;
    private Date requestDate;


}
