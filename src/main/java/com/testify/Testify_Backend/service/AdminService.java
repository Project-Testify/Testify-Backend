package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.Organization;
import com.testify.Testify_Backend.model.VerificationRequest;
import com.testify.Testify_Backend.requests.admin_management.RejectOrganizationRequest;
import com.testify.Testify_Backend.responses.admin.OrganizationGroupResponse;

import java.util.List;

public interface AdminService {

    List<OrganizationGroupResponse> getOrganizationGroup();

    int verifyOrganization(int id);

    int rejectOrganization(RejectOrganizationRequest rejectOrganizationRequest);
}
