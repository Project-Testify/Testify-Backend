package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.exam_management.OrganizationResponse;
import com.testify.Testify_Backend.responses.examsetter_management.ModerateExamResponse;

import java.util.List;
import java.util.Set;

public interface ExamSetterService {
    Set<OrganizationResponse> getOrganizations(long setterId);

    long checkSetterRegistration(String token);

    GenericAddOrUpdateResponse addSetterToOrganization(String token);

    List<ModerateExamResponse> getModeratingExams(long examSetterId);
}
