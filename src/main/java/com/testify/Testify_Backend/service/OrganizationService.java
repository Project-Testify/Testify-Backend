package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.CourseModule;
import com.testify.Testify_Backend.model.ExamSetter;
import com.testify.Testify_Backend.requests.VerificationRequestRequest;
import com.testify.Testify_Backend.requests.organization_management.AddExamSetterRequest;
import com.testify.Testify_Backend.requests.organization_management.CandidateGroupRequest;
import com.testify.Testify_Backend.requests.organization_management.CourseModuleRequest;
import com.testify.Testify_Backend.requests.organization_management.InviteExamSetterRequest;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.GenericDeleteResponse;
import com.testify.Testify_Backend.responses.courseModule.CourseModuleResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Set;

public interface OrganizationService {
    ResponseEntity<GenericAddOrUpdateResponse> addSetterToOrganization(long organizationId, AddExamSetterRequest request);

    GenericAddOrUpdateResponse<VerificationRequestRequest> requestVerification(VerificationRequestRequest verificationRequest) throws IOException;
    GenericAddOrUpdateResponse<CourseModule> addCourseModuleToOrganization(long organizationId, CourseModuleRequest courseModuleRequest);

    Set<CourseModuleResponse> getCourseModulesByOrganization(Long organizationId);
    GenericAddOrUpdateResponse<CandidateGroupRequest> createCandidateGroup(long organizationId, CandidateGroupRequest candidateGroupRequest);
    ResponseEntity<GenericAddOrUpdateResponse> inviteExamSetter(long organizationId, InviteExamSetterRequest request);
}
