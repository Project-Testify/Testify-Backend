package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.exam_management.CandidateResponse;
import com.testify.Testify_Backend.responses.exam_management.ExamResponse;
import com.testify.Testify_Backend.responses.exam_management.OrganizationResponse;
import com.testify.Testify_Backend.service.ExamManagementService;
import com.testify.Testify_Backend.responses.exam_management.ExamResponse;
import com.testify.Testify_Backend.responses.exam_management.OrganizationResponse;
import com.testify.Testify_Backend.responses.examsetter_management.ModerateExamResponse;
import com.testify.Testify_Backend.service.ExamSetterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/exam-setter")
@RequiredArgsConstructor
public class ExamSetterController {
    private static final Logger log = LoggerFactory.getLogger(ExamSetterController.class);
    private final ExamSetterService examSetterService;
    private final ExamManagementService examManagementService;

    @GetMapping("/{setterId}/getOrganizations")
    public ResponseEntity<Set<OrganizationResponse>> getOrganizations(@PathVariable("setterId") long setterId) {
        Set<OrganizationResponse> organizations = examSetterService.getOrganizations(setterId);
        return ResponseEntity.ok(organizations);
    }

    @GetMapping("/{token}/checkSetterRegistration")
    public ResponseEntity<Long> checkSetterRegistration(@PathVariable("token") String token) {
        long setterId = examSetterService.checkSetterRegistration(token);
        return ResponseEntity.ok(setterId);
    }

    @PostMapping("/{token}/addSetterToOrganization")
    public ResponseEntity<GenericAddOrUpdateResponse> addSetterToOrganization(@PathVariable("token") String token){
        log.info("setter reached: {}",token);
        GenericAddOrUpdateResponse response = examSetterService.addSetterToOrganization(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/proctor/{proctorId}/{organizationId}")
    public ResponseEntity<List<ExamResponse>> getExamsForProctor(@PathVariable Long proctorId, @PathVariable Long organizationId) {
        List<ExamResponse> exams = examSetterService.getExamsForProctor(proctorId, organizationId);
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/{examId}/candidates")
    public ResponseEntity<Set<CandidateResponse>> getCandidatesForExam(@PathVariable Long examId) {
        Set<CandidateResponse> candidates = examSetterService.getCandidatesForExam(examId);
        return ResponseEntity.ok(candidates);
    }
    @GetMapping("/{examSetterId}/moderating-exams")
    public ResponseEntity<List<ModerateExamResponse>> getModeratingExams(@PathVariable long examSetterId) {
        log.info("Getting exams moderated by exam setter with ID: {}", examSetterId);
        List<ModerateExamResponse> responses = examSetterService.getModeratingExams(examSetterId);

        // Directly return the list, which will be empty if no exams are found
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{candidateId}/{examId}/proctorComments")
    public ResponseEntity<String> addComment(@PathVariable Long candidateId, @PathVariable Long examId, @RequestBody String content) {
        examSetterService.addCommentToCandidate(candidateId, examId, content);
        return ResponseEntity.ok("Comment added successfully");
    }

}
