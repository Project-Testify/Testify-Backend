package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.model.ExamSetter;
import com.testify.Testify_Backend.requests.VerificationRequestRequest;
import com.testify.Testify_Backend.requests.organization_management.AddExamSetterRequest;
import com.testify.Testify_Backend.requests.organization_management.CandidateGroupRequest;
import com.testify.Testify_Backend.requests.organization_management.CourseModuleRequest;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.GenericDeleteResponse;
import com.testify.Testify_Backend.responses.courseModule.CourseModuleResponse;
import com.testify.Testify_Backend.service.OrganizationService;
import com.testify.Testify_Backend.service.OrganizationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/organization")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationService organizationService;

    @PostMapping("/{organizationId}/exam-setter")
    public ResponseEntity<GenericAddOrUpdateResponse> addSetterToOrganization(@PathVariable long organizationId, @RequestBody AddExamSetterRequest addExamSetterRequest){
        return organizationService.addSetterToOrganization(organizationId, addExamSetterRequest);
    }


    @PostMapping("/{organizationId}/course-module")
    public ResponseEntity<GenericAddOrUpdateResponse> addCourseModuleToOrganization(
            @PathVariable long organizationId,
            @RequestBody CourseModuleRequest courseModuleRequest) {
        GenericAddOrUpdateResponse response = organizationService.addCourseModuleToOrganization(organizationId, courseModuleRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{organizationId}/course-module")
    public ResponseEntity<Set<CourseModuleResponse>> getCourseModulesByOrganization(@PathVariable long organizationId) {
        Set<CourseModuleResponse> courseModules = organizationService.getCourseModulesByOrganization(organizationId);
        return ResponseEntity.ok(courseModules);
    }

    @PostMapping("/{organizationId}/candidate-group")
    public GenericAddOrUpdateResponse<CandidateGroupRequest> createCandidateGroup(
            @PathVariable long organizationId,
            @RequestBody CandidateGroupRequest candidateGroupRequest)
    {
        return organizationService.createCandidateGroup(organizationId, candidateGroupRequest);
    }
}
