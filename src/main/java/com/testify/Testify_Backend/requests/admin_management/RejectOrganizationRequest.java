package com.testify.Testify_Backend.requests.admin_management;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RejectOrganizationRequest {
    private int id;
    private String rejectionReason;
}
