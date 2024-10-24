package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.*;
import com.testify.Testify_Backend.requests.VerificationRequestRequest;
import com.testify.Testify_Backend.requests.organization_management.AddExamSetterRequest;
import com.testify.Testify_Backend.requests.organization_management.CandidateGroupRequest;
import com.testify.Testify_Backend.requests.organization_management.CourseModuleRequest;
import com.testify.Testify_Backend.requests.organization_management.InviteExamSetterRequest;
import com.testify.Testify_Backend.responses.CandidateGroupResponse;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.GenericDeleteResponse;
import com.testify.Testify_Backend.responses.courseModule.CourseModuleResponse;
import com.testify.Testify_Backend.responses.exam_management.ExamResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public interface OrganizationService {
    ResponseEntity<GenericAddOrUpdateResponse> addSetterToOrganization(long organizationId, AddExamSetterRequest request);

    GenericAddOrUpdateResponse<VerificationRequestRequest> requestVerification(VerificationRequestRequest verificationRequest) throws IOException;
    GenericAddOrUpdateResponse<CourseModule> addCourseModuleToOrganization(long organizationId, CourseModuleRequest courseModuleRequest);

    Set<CourseModuleResponse> getCourseModulesByOrganization(Long organizationId);
    GenericAddOrUpdateResponse<CandidateGroupRequest> createCandidateGroup(long organizationId, CandidateGroupRequest candidateGroupRequest);
    Set<CandidateGroupResponse> getCandidateGroupsByOrganization(Long organizationId);

    GenericAddOrUpdateResponse addCandidateToGroup(long groupId, String name, String email);
    GenericDeleteResponse deleteGroup(long groupId);

    //Optional<CandidateGroup> getCandidateGroupsByOrganization(long organizationId);

    ResponseEntity<GenericAddOrUpdateResponse> inviteExamSetter(long organizationId, InviteExamSetterRequest request);
    String confirmToken(String token);

    GenericDeleteResponse deleteCandidate(long groupId, long candidateId);

    GenericAddOrUpdateResponse updateCandidateGroup(long groupId, String groupName);

    Set<ExamSetter> getExamSetters(long organizationId);

    Set<ExamSetterInvitation> getExamSetterInvitations(long organizationId);

    Set<ExamResponse> getExams(long organizationId);
}
