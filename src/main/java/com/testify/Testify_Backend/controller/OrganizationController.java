package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.model.CandidateGroup;
import com.testify.Testify_Backend.model.Exam;
import com.testify.Testify_Backend.model.ExamSetter;
import com.testify.Testify_Backend.model.ExamSetterInvitation;
import com.testify.Testify_Backend.requests.VerificationRequestRequest;
import com.testify.Testify_Backend.requests.organization_management.AddExamSetterRequest;
import com.testify.Testify_Backend.requests.organization_management.CandidateGroupRequest;
import com.testify.Testify_Backend.requests.organization_management.CourseModuleRequest;
import com.testify.Testify_Backend.requests.organization_management.InviteExamSetterRequest;
import com.testify.Testify_Backend.responses.CandidateGroupResponse;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.GenericDeleteResponse;
import com.testify.Testify_Backend.responses.courseModule.CourseModuleResponse;
import com.testify.Testify_Backend.responses.exam_management.CandidateGroupSearchResponse;
import com.testify.Testify_Backend.responses.exam_management.ExamResponse;
import com.testify.Testify_Backend.responses.exam_management.ExamSetterResponse;
import com.testify.Testify_Backend.responses.organization_management.ExamSetterSearchResponse;
import com.testify.Testify_Backend.service.ExamManagementService;
import com.testify.Testify_Backend.service.OrganizationService;
import com.testify.Testify_Backend.service.OrganizationServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/v1/organization")
@RequiredArgsConstructor
public class OrganizationController {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);
    private final OrganizationService organizationService;
    private final ExamManagementService examManagementService;

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
    public  ResponseEntity<GenericAddOrUpdateResponse<CandidateGroupRequest>> createCandidateGroup(
            @PathVariable long organizationId, @RequestBody CandidateGroupRequest candidateGroupRequest)
    {
        logger.info("Received request to create candidate group for organization ID: {}", organizationId);
        logger.info("Candidate Group Request: {}", candidateGroupRequest);
        GenericAddOrUpdateResponse<CandidateGroupRequest> response = organizationService.createCandidateGroup(organizationId, candidateGroupRequest);

        return ResponseEntity.ok(response);

    }

    @PostMapping("/{groupId}/add-candidate")
    public ResponseEntity<GenericAddOrUpdateResponse> addCandidateToGroup(@PathVariable long groupId, @RequestParam String name, @RequestParam String email){
        GenericAddOrUpdateResponse response = organizationService.addCandidateToGroup(groupId, name, email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{groupId}/delete-group")
    public ResponseEntity<GenericDeleteResponse> deleteGroup(@PathVariable long groupId){
        GenericDeleteResponse response = organizationService.deleteGroup(groupId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{groupId}/delete-candidate")
    public ResponseEntity<GenericDeleteResponse> deleteCandidate(@PathVariable long groupId, @RequestParam long candidateId){
        GenericDeleteResponse response = organizationService.deleteCandidate(groupId, candidateId);
        logger.info("deleted");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{groupId}/update-candidateGroup")
    public ResponseEntity<GenericAddOrUpdateResponse> updateCandidateGroup(@PathVariable long groupId, @RequestParam String groupName){
        GenericAddOrUpdateResponse response = organizationService.updateCandidateGroup(groupId, groupName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{organizationId}/candidate-group")
    public ResponseEntity<Set<CandidateGroupResponse>> getCandidateGroups(@PathVariable long organizationId){
       Set<CandidateGroupResponse> candidateGroups = organizationService.getCandidateGroupsByOrganization(organizationId);
       return ResponseEntity.ok(candidateGroups);
    }

    @PostMapping("/{organizationId}/invite-exam-setter")
    public ResponseEntity<GenericAddOrUpdateResponse> inviteExamSetter(
            @PathVariable long organizationId, @RequestBody InviteExamSetterRequest request) {
        return organizationService.inviteExamSetter(organizationId, request);
    }

    @PostMapping("/confirm")
    public String verifyEmail(@RequestParam("invitation") String token) {
        logger.info(token);
        return organizationService.confirmToken(token);
    }

    //get exam setters by organization
    @GetMapping("/{organizationId}/examSetters")
    public ResponseEntity<Set<ExamSetter>> getExamSetters(@PathVariable long organizationId){
        Set<ExamSetter> examSetters = organizationService.getExamSetters(organizationId);
        return ResponseEntity.ok(examSetters);
    }

    @GetMapping("/{organizationId}/search-exam-setters")
    public ResponseEntity<?> getExamSettersByOrganizationId(@PathVariable Long organizationId) {
        return organizationService.getExamSettersForSearchByOrganizationId(organizationId);
    }

    //get exam setter invited invitations
    @GetMapping("/{organizationId}/invitations")
    public ResponseEntity<Set<ExamSetterInvitation>> getExamSetterInvitations(@PathVariable long organizationId){
        Set<ExamSetterInvitation> invitations = organizationService.getExamSetterInvitations(organizationId);
        return ResponseEntity.ok(invitations);
    }

    @GetMapping("/{organizationId}/exams")
    public ResponseEntity<Set<ExamResponse>> getExams(@PathVariable long organizationId){
        Set<ExamResponse> exams = organizationService.getExams(organizationId);
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/{organizationId}/candidate-groups-search")
    public ResponseEntity<List<CandidateGroupSearchResponse>> getCandidateGroupsByOrganizationForSearch(
            @PathVariable Long organizationId) {
        List<CandidateGroupSearchResponse> candidateGroups = examManagementService.getCandidateGroupsByOrganizationForSearch(organizationId);
        return ResponseEntity.ok(candidateGroups);
    }

}
