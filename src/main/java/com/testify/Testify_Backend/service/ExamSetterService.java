package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.exam_management.OrganizationResponse;

import java.util.Set;

public interface ExamSetterService {
    Set<OrganizationResponse> getOrganizations(long setterId);

    long checkSetterRegistration(String token);

    GenericAddOrUpdateResponse addSetterToOrganization(String token);
}
